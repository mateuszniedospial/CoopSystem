import {Component, OnInit, OnDestroy} from "@angular/core";
import {Project} from "./project.model";
import {ActivatedRoute} from "@angular/router";
import {ProjectService} from "./project.service";
import {Principal} from "../../shared/auth/principal.service";
import {JUserService} from "../j-user/j-user.service";
import {JUser} from "../j-user/j-user.model";
import {JGroup} from "../j-group/j-group.model";
import {JGroupService} from "../j-group/j-group.service";
import {Response} from "@angular/http";
import {Message} from "primeng/components/common/api";
import {JGroupImg} from "../j-group-img/j-group-img.model";
import {JGroupImgService} from "../j-group-img/j-group-img.service";
import {DateUtil} from "../../shared/util/date-util";
import {Subscription} from "rxjs";
import {EventManager} from "ng-jhipster";
import {ProjectImgService} from "../project-img/project-img.service";
import {ProjectImg} from "../project-img/project-img.model";
/**
 * Created by Chrono on 10.06.2017.
 */


@Component({
    selector: 'project-view',
    templateUrl: './project-view.component.html',
    styleUrls: [
        'project-view.css'
    ]
})
export class ProjectViewComponent implements OnInit, OnDestroy {

    public dateUtilStaticRef : DateUtil = DateUtil;

    project: Project;
    projectID: string;
    projectImg: ProjectImg;

    projectJGroups: JGroup[] = [];

    jUser: JUser;
    jUserJGroups: JGroup[] = [];
    jGroupsImgMap:{[key:string]:JGroupImg;} = {};

    hasPermissions: boolean = false;

    private subscription: any;
    msgs: Message[] = [];
    eventSubscriber: Subscription;

    constructor(
        private principal: Principal,
        private route: ActivatedRoute,
        private projectService: ProjectService,
        private projectImgService: ProjectImgService,
        private jUserService: JUserService,
        private jGroupService: JGroupService,
        private jGroupImgService: JGroupImgService,
        private eventManager: EventManager,
    ) {
    }

    ngOnInit(){
        this.initializeData();
        this.registerEvents();
    }

    private registerEvents() {
        this.registerChangeInProject();
        this.registerChangeInJGroups();
        this.registerChangeInProjectImg();
        this.registerChangeInProjectGroups();
        this.registerChangeInProjectList();
    }

    initializeData(){
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
            this.projectID = params['id'];
        });

        this.principal.identity().then((user) => {
            this.jUserService.queryJUser(user.id).subscribe(jUser => {
                this.jUser = jUser;
                this.queryJGroups(jUser.id);
            });
        });
    }

    load (id) {
        this.projectService.find(id).subscribe(project => {
            this.project = project;
            this.projectJGroups = project.jgroups;
            this.queryProjectImg(project.id);
            for(let jGroup of this.projectJGroups) {
                this.queryJGroupImgs(jGroup.id);
            }
        });
    }

    reloadProjectJGroups(id){
        this.projectService.find(id).subscribe(project => {
            this.project = project;
            this.projectJGroups = project.jgroups;
        });
    }

    queryProjectImg(id){
        this.projectImgService.findImgOfProject(id).subscribe(projectImg => {
            this.projectImg = projectImg;
        });
    }

    initProjectReload(){
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
            this.projectID = params['id'];
        });
    }

    initJGroupsReload(){
        this.projectJGroups = this.project.jgroups;
        for(let jGroup of this.projectJGroups) {
            this.queryJGroupImgs(jGroup.id);
        }
    }

    queryJGroups(id){
        this.jGroupService.querySpecificUserGroups(id).subscribe((res: Response) => {
            this.jUserJGroups = res.json();
            this.savePermissions();
        });
    }

    queryJGroupImgs(id) {
        this.jGroupImgService.queryJImg(id).subscribe(jGroupImg => {
            this.jGroupsImgMap[id] = jGroupImg;
        });
    }

    savePermissions(){
        for(let jGroup of this.projectJGroups){
            for(let jgroup of this.jUserJGroups){
                if(jGroup.id == jgroup.id){
                    this.hasPermissions = true;
                }
            }
        }
    }

    // checkPermissions(){
    //     for(let jGroup of this.projectJGroups){
    //         for(let jgroup of this.jUserJGroups){
    //             if(jGroup.id == jgroup.id){
    //                 return true;
    //             }
    //         }
    //     }
    //     return false;
    // }

    checkPermissions(){
        return this.hasPermissions;
    }

    saveMethod(obj) {
        if (obj.description){
            this.project.description = obj.description;
            this.projectService.update(this.project)
                .subscribe(
                    (res) => {
                        this.createSuccessAlert('Change of the project description has been saved successfully!');
                    },
                    () => {
                        this.createErrorAlert('Your changes have not been saved due to some error.');
                    }
                );
        } else if(obj.name){
            this.project.name = obj.name;
            this.projectService.update(this.project)
                .subscribe(
                    (res) => {
                        this.createSuccessAlert('Change of the project description has been saved successfully!');
                    },
                    () => {
                        this.createErrorAlert('Your changes have not been saved due to some error.');
                    }
                );
        }
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

    registerChangeInJGroups(){
        this.eventSubscriber = this.eventManager.subscribe('jGroupListModification', () => {
            this.initJGroupsReload();
        });
    }

    registerChangeInProjectList(){
        this.eventSubscriber = this.eventManager.subscribe('projectListModification', () => {
            this.initProjectReload();
        });
    }

    registerChangeInProject(){
        this.eventSubscriber = this.eventManager.subscribe('jGroupListModification', () => {
            this.initProjectReload();
        });
    }

    registerChangeInProjectImg(){
        this.eventSubscriber = this.eventManager.subscribe('projectImgListModification', () => {
            this.queryProjectImg(this.project.id);
        });
    }

    registerChangeInProjectGroups(){
        this.eventSubscriber = this.eventManager.subscribe('projectGroupsListModification', () => {
                this.initializeData();
        });
    }

    ngOnDestroy(){
        this.eventManager.destroy(this.eventSubscriber);
    }
}
