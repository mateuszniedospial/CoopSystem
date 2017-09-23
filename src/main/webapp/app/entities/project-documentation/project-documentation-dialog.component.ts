import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, DataUtils } from 'ng-jhipster';

import { ProjectDocumentation } from './project-documentation.model';
import { ProjectDocumentationPopupService } from './project-documentation-popup.service';
import { ProjectDocumentationService } from './project-documentation.service';
import { JUser, JUserService } from '../j-user';
import { Project, ProjectService } from '../project';
import { DocumentationCatalogue, DocumentationCatalogueService } from '../documentation-catalogue';
@Component({
    selector: 'jhi-project-documentation-dialog',
    templateUrl: './project-documentation-dialog.component.html'
})
export class ProjectDocumentationDialogComponent implements OnInit {

    projectDocumentation: ProjectDocumentation;
    authorities: any[];
    isSaving: boolean;

    lastauthors: JUser[];

    projects: Project[];

    documentationcatalogues: DocumentationCatalogue[];
    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: DataUtils,
        private alertService: AlertService,
        private projectDocumentationService: ProjectDocumentationService,
        private jUserService: JUserService,
        private projectService: ProjectService,
        private documentationCatalogueService: DocumentationCatalogueService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.jUserService.query({filter: 'projectdocumentation-is-null'}).subscribe((res: Response) => {
            if (!this.projectDocumentation.lastAuthor || !this.projectDocumentation.lastAuthor.id) {
                this.lastauthors = res.json();
            } else {
                this.jUserService.find(this.projectDocumentation.lastAuthor.id).subscribe((subRes: JUser) => {
                    this.lastauthors = [subRes].concat(res.json());
                }, (subRes: Response) => this.onError(subRes.json()));
            }
        }, (res: Response) => this.onError(res.json()));
        this.projectService.query().subscribe(
            (res: Response) => { this.projects = res.json(); }, (res: Response) => this.onError(res.json()));
        this.documentationCatalogueService.query().subscribe(
            (res: Response) => { this.documentationcatalogues = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData($event, projectDocumentation, field, isImage) {
        if ($event.target.files && $event.target.files[0]) {
            let $file = $event.target.files[0];
            if (isImage && !/^image\//.test($file.type)) {
                return;
            }
            this.dataUtils.toBase64($file, (base64Data) => {
                projectDocumentation[field] = base64Data;
                projectDocumentation[`${field}ContentType`] = $file.type;
            });
        }
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.projectDocumentation.id !== undefined) {
            this.projectDocumentationService.update(this.projectDocumentation)
                .subscribe((res: ProjectDocumentation) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.projectDocumentationService.create(this.projectDocumentation)
                .subscribe((res: ProjectDocumentation) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: ProjectDocumentation) {
        this.eventManager.broadcast({ name: 'projectDocumentationAddModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError (error) {
        this.isSaving = false;
        this.onError(error);
    }

    private onError (error) {
        this.alertService.error(error.message, null, null);
    }

    trackJUserById(index: number, item: JUser) {
        return item.id;
    }

    trackProjectById(index: number, item: Project) {
        return item.id;
    }

    trackDocumentationCatalogueById(index: number, item: DocumentationCatalogue) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-project-documentation-popup',
    template: ''
})
export class ProjectDocumentationPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private projectDocumentationPopupService: ProjectDocumentationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.projectDocumentationPopupService
                    .open(ProjectDocumentationDialogComponent, params['id']);
            } else if (params['projectid']){
                this.modalRef = this.projectDocumentationPopupService
                    .openCreate(ProjectDocumentationDialogComponent, params);
            } else {
                this.modalRef = this.projectDocumentationPopupService
                    .open(ProjectDocumentationDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
