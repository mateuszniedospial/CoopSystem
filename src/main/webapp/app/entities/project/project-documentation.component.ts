import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {ProjectService} from "./project.service";
import {Project} from "./project.model";
import {TreeNode, Message, SelectItem} from "primeng/components/common/api";
import {Principal} from "../../shared/auth/principal.service";
import {JUserService} from "../j-user/j-user.service";
import {JUser} from "../j-user/j-user.model";
import {DocumentationCatalogueService} from "../documentation-catalogue/documentation-catalogue.service";
import {ProjectDocumentationService} from "../project-documentation/project-documentation.service";
import {DocumentationCatalogue} from "../documentation-catalogue/documentation-catalogue.model";
import {ProjectDocumentation} from "../project-documentation/project-documentation.model";
import {EventManager} from "ng-jhipster";
import {Subscription, Observable} from "rxjs";
import {DocumentationHistory} from "../documentation-history/documentation-history.model";
import {DocumentationHistoryService} from "../documentation-history/documentation-history.service";
import {Response} from "@angular/http";
import {JGroup} from "../j-group/j-group.model";
import {JGroupService} from "../j-group/j-group.service";
import {ScriptLazyLoader} from "../../shared/util/scritp-lazy-loader";
/**
 * Created by Chrono on 31.05.2017.
 */

// let jsPDF = require('jspdf');

@Component({
    selector: 'project-documentation',
    templateUrl: './project-documentation.component.html',
    styleUrls: [
        'project-documentation.css'
    ]
})
export class ProjectDocumentationComponent implements OnInit, OnDestroy {

    project: Project;
    projectID: number;

    jUser: JUser;
    hasPermissionToEdit: boolean = false;
    hasPermissionToRead: boolean = false;

    docsNCatalogues: TreeNode[];
    selectedFile: TreeNode;

    selectedFolder: DocumentationCatalogue;
    selectedDoc: ProjectDocumentation;

    docSelectedAtStart: ProjectDocumentation;
    copyDocSelectedAtStart: ProjectDocumentation;

    selectedFileLabel:string;

    docHistoryVersions: DocumentationHistory[];

    versions: SelectItem[] = [];
    selectedVersion: string;

    selectedHistoryVersion: DocumentationHistory;

    historySelectedAtStart: DocumentationHistory;
    copyHistorySelectedAtStart: DocumentationHistory;

    editMode: boolean = false;

    docWasUpdatedInTheMeantime: boolean = false;

    historyWasUpdatedInTheMeantime: boolean = false;

    isInProgress:boolean = false;

    label:string;
    private subscription: any;

    eventSubscriber: Subscription;
    msgs: Message[] = [];

    constructor(
        private principal: Principal,
        private jUserService: JUserService,
        private jGroupService: JGroupService,
        private projectService: ProjectService,
        private documentationCatalogueService: DocumentationCatalogueService,
        private projectDocumentationService: ProjectDocumentationService,
        private historyDocumentationService: DocumentationHistoryService,
        private eventManager: EventManager,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        ScriptLazyLoader.loadScript('/scripts/quill.js')
        ScriptLazyLoader.loadCss('/css/quill.core.css')
        ScriptLazyLoader.loadCss('/css/quill.snow.css')
        this.initializeData();
        this.loadAllDocsNCatalogues();
        this.listenToEvents();
    }

    listenToEvents(){
        this.registerChangeInCatalogues();
        this.registerChangeInDocuments();
        this.registerChangeInDocumentContent();
        this.registerChangeInDocumentRevert();
        this.registerChangeInHistoryContent();
        this.registerChangeInHistoryList();
        this.registerChangeInDocumentDelete();
        this.registerChangeInDocumentAdd();
        this.registerChangeInDocumentAndHistoryContent();
    }

    initializeData(){
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
            this.projectID = params['id'];
        });

        this.principal.identity().then((user) => {
            this.jUserService.queryJUser(user.id).subscribe(jUser => {
                this.jUser = jUser;
                // this.savePermissions(jUser.id);
        })
        });
    }

    load (id) {
        this.projectService.find(id).subscribe(project => {
            this.project = project;
        });
    }

    private loadAllDocsNCatalogues(){
        this.projectService.getAllDocs(this.projectID).then(allDocs => {
            this.docsNCatalogues = allDocs;
        });
    }

    eventSelectedFile(obj){
        this.isInProgress = true;
        this.savePermissions(this.jUser.id);
        this.selectedFileLabel = obj.node.label;
        if(obj.node.expandedIcon == "fa-folder-open"){
            this.documentationCatalogueService.queryByLabel(obj.node.label).subscribe(docFolder => {
                this.selectedFolder = docFolder;
                this.selectedDoc = null;
                this.isInProgress = false;
            });
        } else if(obj.node.icon == "fa-file-word-o") {
            this.projectDocumentationService.queryByLabel(obj.node.label).subscribe(doc => {
                this.selectedDoc = doc;
                this.docSelectedAtStart = doc;
                let copy = new ProjectDocumentation();
                copy.content = doc.content;
                this.copyDocSelectedAtStart = copy;
                this.loadDocumentationHistoryVersions(doc.id);
                this.selectedFolder = null;
                this.isInProgress = false;
            });
        }
    }

    private savePermissions(id){
        let jGroups:JGroup[] = this.project.jgroups;
        let jUserGroups:JGroup[] = [];
        this.jGroupService.querySpecificUserGroups(id).subscribe((res: Response) => {
            jUserGroups = res.json();
            for(let jGroup of jGroups){
                for(let jUserJGroup of jUserGroups){
                    if(jGroup.id === jUserJGroup.id){
                        this.hasPermissionToEdit = true;
                        this.hasPermissionToRead = true;
                    }
                }
            }
        });
    }

    private reloadSelectedDocContent(){
        this.projectDocumentationService.queryByLabel(this.selectedFileLabel).subscribe(doc => {
            this.selectedDoc = doc;
            this.docSelectedAtStart = doc;
            let copy = new ProjectDocumentation();
            copy.content = doc.content;
            this.copyDocSelectedAtStart = copy;
            this.selectedFolder = null;
        });
    }

    private reloadSelectedDocDueToRevertOp(){
        this.projectDocumentationService.queryByLabel(this.selectedFileLabel).subscribe(doc => {
            this.selectedDoc = doc;
            this.docSelectedAtStart = doc;
            let copy = new ProjectDocumentation();
            copy.content = doc.content;
            this.copyDocSelectedAtStart = copy;
            this.selectedFolder = null;
            this.loadDocumentationHistoryVersions(doc.id);
        });
    }

    private unselectAll(){
        this.selectedDoc = null;
        this.docSelectedAtStart = null;
        this.copyDocSelectedAtStart = null;
        this.selectedFolder = null;
        this.selectedHistoryVersion = null;
        this.historySelectedAtStart = null;
        this.copyHistorySelectedAtStart = null;
        this.editMode = false;
    }

    private reloadHistoryVersions(){
        this.loadDocumentationHistoryVersions(this.selectedDoc.id);
    }

    private reloadHistories(){
        this.loadDocumentationHistoryVersions(this.selectedDoc.id);
    }

    private loadDocumentationHistoryVersions(documentID){
        this.selectedHistoryVersion = null;
        this.docHistoryVersions = [];
        this.versions = [];
        this.historyDocumentationService.queryPreviousVersionsOfDocument(documentID).subscribe((res: Response) => {
            this.docHistoryVersions = res.json();
            for(let docHistory of this.docHistoryVersions){
                this.versions.push({label: docHistory.version.toString(), value: docHistory.version.toString()});
            }
            this.versions.reverse();
        });
    }

    // saveMethod(obj, docAtStart: ProjectDocumentation, author: JUser) {
    //     if (obj.documentVersion){
    //         this.projectDocumentationService.queryByLabel(this.selectedFileLabel).subscribe(doc => {
    //             if(docAtStart.content != doc.content){
    //                 this.docWasUpdatedInTheMeantime = true;
    //                 this.docSelectedAtStart = doc;
    //                 let copy = new ProjectDocumentation();
    //                 copy.content = doc.content;
    //                 this.copyDocSelectedAtStart = copy;
    //             } else {
    //                 this.createDocumentationHistory(author);
    //                 this.selectedDoc.version = obj.documentVersion;
    //                 this.selectedDoc.lastAuthor = author;
    //                 this.projectDocumentationService.update(this.selectedDoc)
    //                     .subscribe(
    //                         (res) => {
    //                             this.createSuccessAlert('Change of the document version has been saved successfully!');
    //                             this.onSaveSuccessDocumentation(res);
    //                         },
    //                         () => {
    //                             this.createErrorAlert('Your changes have not been saved due to some error.');
    //                         });
    //             }
    //         });
    //     }
    // }

    saveVersion(docAtStart: ProjectDocumentation, author: JUser) {
        this.projectDocumentationService.queryByLabel(this.selectedFileLabel).subscribe(doc => {
                if(docAtStart.content != doc.content){
                    this.docWasUpdatedInTheMeantime = true;
                    this.docSelectedAtStart = doc;
                    let copy = new ProjectDocumentation();
                    copy.content = doc.content;
                    this.copyDocSelectedAtStart = copy;
                } else {
                    this.isInProgress = true;
                    this.createDocumentationHistory(author);
                    let newVersion:number = this.selectedDoc.version+1;
                    this.selectedDoc.version = newVersion;
                    this.selectedDoc.lastAuthor = author;
                    this.projectDocumentationService.update(this.selectedDoc)
                        .subscribe(
                            (res) => {
                                this.isInProgress = false;
                                this.createSuccessAlert('Change of the document version has been saved successfully!');
                                this.onSaveSuccessDocumentation(res);
                            },
                            () => {
                                this.createErrorAlert('Your changes have not been saved due to some error.');
                            });
                }
            });
    }

    checkPermissions() {
        return this.hasPermissionToEdit && this.editMode;
    }

    private createDocumentationHistory(author: JUser){
        let newDocHist:DocumentationHistory = new DocumentationHistory;
        newDocHist.createdDate = this.selectedDoc.createdDate;
        newDocHist.modifyDate = this.selectedDoc.modifyDate;
        newDocHist.content = this.selectedDoc.content;
        if(this.selectedDoc.version == null){
            newDocHist.version = 0;
        } else {
            newDocHist.version = this.selectedDoc.version;
        }
        newDocHist.projectDocumentation = this.selectedDoc;
        newDocHist.lastAuthor = author;
        this.historyDocumentationService.create(newDocHist).subscribe(
            (res: DocumentationHistory) => this.onSaveSuccess(res),
            (res: Response) => this.onSaveError(res.json()));
    }

    versionSelect(obj){
        for(let docVersion of this.docHistoryVersions){
            if(docVersion.version == +this.selectedVersion){
                this.selectedHistoryVersion = docVersion;
                let copy = new DocumentationHistory();
                copy.id = docVersion.id;
                copy.content = docVersion.content;
                this.copyHistorySelectedAtStart = copy;
            }
        }
    }

    returnToDocument(){
        this.selectedHistoryVersion = null;
        this.selectedVersion = null;
    }

    saveDocument(document: ProjectDocumentation, author: JUser){
        let startDoc: ProjectDocumentation = document;
        this.projectDocumentationService.queryByLabel(this.selectedFileLabel).subscribe(doc => {
            if(startDoc.content != doc.content){
                this.docWasUpdatedInTheMeantime = true;
                this.docSelectedAtStart = doc;
                let copy = new ProjectDocumentation();
                copy.content = doc.content;
                this.copyDocSelectedAtStart = copy;
            } else if(this.docWasUpdatedInTheMeantime) {
                this.saveContentFromEditor(this.docSelectedAtStart, author);
                this.docWasUpdatedInTheMeantime = false;
            } else {
                this.saveContentFromEditor(this.selectedDoc, author);
            }
        });
    }

    private saveContentFromEditor(document: ProjectDocumentation, author: JUser){
        this.isInProgress = true;
        document.lastAuthor = author;
        this.projectDocumentationService.update(document).subscribe(
            (res: ProjectDocumentation) => {
                this.isInProgress = false;
                this.createSuccessAlert('Your changes to current document have been saved successfully!');
                this.onSaveSuccessDocumentation(res);
            },
            (res: Response) => {
                this.isInProgress = false;
                this.createErrorAlert('Your changes have not been saved due to some error.');
                this.onSaveError(res.json());
            }
        );
    }

    saveDocumentHistory(history: DocumentationHistory, author: JUser){
        let startHistory: DocumentationHistory = history;
        this.historyDocumentationService.find(history.id).subscribe(doc => {
            if(startHistory.content != doc.content){
                this.historyWasUpdatedInTheMeantime = true;
                this.historySelectedAtStart = doc;
                let copy = new DocumentationHistory();
                copy.content = doc.content;
                this.copyHistorySelectedAtStart = copy;
            } else if(this.historyWasUpdatedInTheMeantime) {
                this.saveContentFromEditorToHistory(this.historySelectedAtStart, author);
                this.historyWasUpdatedInTheMeantime = false;
            } else {
                this.saveContentFromEditorToHistory(this.selectedHistoryVersion, author);
            }
        });
    }

    private saveContentFromEditorToHistory(history: DocumentationHistory, author: JUser){
        this.isInProgress = true;
        history.lastAuthor = author;
        this.historyDocumentationService.update(history).subscribe(
            (res: DocumentationHistory) => {
                this.isInProgress = false;
                this.createSuccessAlert('Your changes to selected version have been saved successfully!');
            },
            (res: Response) => {
                this.isInProgress = false;
                this.createErrorAlert('Your changes have not been saved due to some error.');
                this.onSaveError(res.json());
            }
        );
    }

    documentToPDF(){
        // const pdfContent = this.selectedDoc.content;
        // // const pdf = new jsPDF('p', 'pt', 'letter');
        //
        // pdf.fromHTML(pdfContent,25,25, {
        //     'width': 550
        // });
        //
        // pdf.save(this.selectedDoc.label + '.pdf');
    }

    historyToPDF(){
        // const pdfContent = this.selectedHistoryVersion.content;
        // const pdf = new jsPDF('p', 'pt', 'letter');
        //
        // pdf.fromHTML(pdfContent,25,25, {
        //     'width': 550
        // });
        //
        // pdf.save(this.selectedDoc.label + ' version ' + this.selectedHistoryVersion.version + '.pdf');
    }

    returnFromMerge(){
        if(this.docWasUpdatedInTheMeantime){
            this.docWasUpdatedInTheMeantime = false;
            this.copyDocSelectedAtStart = null;
        } else if(this.historyWasUpdatedInTheMeantime){
            this.historyWasUpdatedInTheMeantime = false;
            this.copyHistorySelectedAtStart = null;
        }
    }

    private onSaveSuccess (result: DocumentationHistory) {
        this.eventManager.broadcast({ name: 'projectHistoryContentModification', content: 'OK'});
    }

    private onSaveSuccessDocumentation (result: ProjectDocumentation) {
        this.eventManager.broadcast({ name: 'projectDocumentationContentAndHistoryModification', content: 'OK'});
    }

    private onSaveError (error) {
        this.createErrorAlert('Your changes have not been saved due to some error.')
    }

    createSuccessAlert(message: string) {
        this.msgs.push({severity:'success', summary:'Success', detail:message});
        setTimeout(()=>{
            this.msgs=[];
        },4000);
    }

    createErrorAlert(message: string) {
        this.msgs.push({severity:'error', summary:'Error', detail:message});
        setTimeout(()=>{
            this.msgs=[];
        },4000);
    }

    registerChangeInCatalogues(){
        this.eventSubscriber = this.eventManager.subscribe('documentationCatalogueListModification', () => {
            this.loadAllDocsNCatalogues();
        });
    }

    registerChangeInDocuments(){
        this.eventSubscriber = this.eventManager.subscribe('projectDocumentationListModification', () => {
            this.loadAllDocsNCatalogues();
            this.reloadHistories();
        });
    }

    registerChangeInDocumentContent(){
        this.eventSubscriber = this.eventManager.subscribe('projectDocumentationContentModification', () => {
            this.reloadSelectedDocContent();
        })
    }

    registerChangeInDocumentAndHistoryContent(){
        this.eventSubscriber = this.eventManager.subscribe('projectDocumentationContentAndHistoryModification', () => {
            this.reloadSelectedDocContent();
            // this.reloadHistories();
        })
    }

    registerChangeInDocumentRevert(){
        this.eventSubscriber = this.eventManager.subscribe('projectDocumentationRevertModification', () => {
            this.reloadSelectedDocDueToRevertOp();
        })
    }

    registerChangeInDocumentDelete(){
        this.eventSubscriber = this.eventManager.subscribe('projectDocumentationDeleteModification', () => {
            this.unselectAll();
            this.initializeData();
            this.loadAllDocsNCatalogues();
        })
    }

    registerChangeInDocumentAdd(){
        this.eventSubscriber = this.eventManager.subscribe('projectDocumentationAddModification', () => {
            this.loadAllDocsNCatalogues();
        })
    }

    registerChangeInHistoryContent(){
        this.eventSubscriber = this.eventManager.subscribe('projectHistoryContentModification', () => {
            this.reloadHistoryVersions();
        })
    }

    registerChangeInHistoryList(){
        this.eventSubscriber = this.eventManager.subscribe('documentationHistoryListModification', () => {
            this.reloadHistories;
            this.returnToDocument();
            // this.initializeData();
        })
    }

    goToEdit(){
        this.editMode = true;
        this.reloadSelectedDocContent();
    }

    goToRead(){
        this.editMode = false;
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }
}
