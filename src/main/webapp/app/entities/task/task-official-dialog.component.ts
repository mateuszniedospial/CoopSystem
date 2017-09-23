import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import {Task, TaskLifeCycle} from './task.model';
import { TaskPopupService } from './task-popup.service';
import { TaskService } from './task.service';
import { Sprint, SprintService } from '../sprint';
import { JGroup, JGroupService } from '../j-group';
import { JUser, JUserService } from '../j-user';
import {Principal} from "../../shared/auth/principal.service";
import {TaskOfficialDetailComponent} from "./task-official-detail.component";
import {TaskDescription} from "../task-description/task-description.model";
import {TaskDescriptionService} from "../task-description/task-description.service";
import {Project} from "../project/project.model";
import {ProjectService} from "../project/project.service";

@Component({
    selector: 'task-official-dialog',
    templateUrl: './task-official-dialog.component.html'
})
export class TaskOfficialDialogComponent implements OnInit {

    task: Task;
    authorities: any[];
    isSaving: boolean;
    sprints: Sprint[];
    private accountTask:any
    private activJUser: JUser;
    projects: Project[] = [];
    jgroups: JGroup[];
    jGroupRequired: boolean = true;
    projectRequired: boolean = true;
    hasNotPermissionToAdd: boolean = false;
    hasPermissionToAdd: boolean = true;
    taskDescription: TaskDescription;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private taskService: TaskService,
        private sprintService: SprintService,
        private jGroupService: JGroupService,
        private projectService: ProjectService,
        private jUserService: JUserService,
        private eventManager: EventManager,
        private principal: Principal,
        private taskDescriptionService: TaskDescriptionService
    ) {
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.accountTask = TaskOfficialDetailComponent.copyAccount(account);
            this.initJUserIDByJhiId(account.id);
        });
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.sprintService.query({filter: 'task-is-null'}).subscribe((res: Response) => {
            if (!this.task.sprint || !this.task.sprint.id) {
                this.sprints = res.json();
            } else {
                this.sprintService.find(this.task.sprint.id).subscribe((subRes: Sprint) => {
                    this.sprints = [subRes].concat(res.json());
                }, (subRes: Response) => this.onError(subRes.json()));
            }
        }, (res: Response) => this.onError(res.json()));
        if(this.task.jgroup) {
            this.jGroupRequired = false;
        }
        this.task.estimateTime="?";
        this.taskDescription = new TaskDescription();
        this.taskDescription.content="";
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        this.task.reporter = this.activJUser;
        this.task.assignee = this.activJUser;
        this.task.lifeCycle = TaskLifeCycle.TODO;
        if (this.task.id !== undefined) {
            this.taskService.update(this.task)
                .subscribe((res: Task) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.taskService.create(this.task)
                .subscribe((res: Task) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: Task) {
        this.taskDescription.task = result;
        this.taskDescriptionService.create(this.taskDescription)
            .subscribe(
                (res:Response)=>{//do nothing
                     },
                (res:Response)=>{this.onSaveError(res.json())})
        this.eventManager.broadcast({ name: 'taskListModification', content: 'OK'});
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

    trackSprintById(index: number, item: Sprint) {
        return item.id;
    }

    trackTaskById(index: number, item: Task) {
        return item.id;
    }

    trackJGroupById(index: number, item: JGroup) {
        return item.id;
    }

    trackJUserById(index: number, item: JUser) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }

    private initJUserIDByJhiId(jhiId : number ){
        this.jUserService.query({
            'jhiId' : jhiId
        }).subscribe((res: Response) => {
                this.activJUser =res.json();
                this.initJGroup();
            },
            (res: Response) => this.onError(res.json()))
    }

    private initJGroup() {
        this.jGroupService.queryJUserId({
            'query' : this.activJUser.id
        }).subscribe((res: Response) => {
            let response : any[] = res.json()
            this.jgroups = response;
            this.initProjects();
            this.hasPermissionToAdd = response.length > 0;
            this.hasNotPermissionToAdd = !this.hasPermissionToAdd;
        },
            (res: Response) => this.onError(res.json())
        );
    }

    private initProjects(){
        for(let jgroup of this.jgroups){
            this.projectService.queryByGroupId(jgroup.id).subscribe(res => {
                for(let project of res.json()){
                    this.projects.push(project);
                }
            })
        }
    }
}

@Component({
    selector: 'jhi-task-popup',
    template: ''
})
export class TaskOfficialPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;


    constructor (
        private route: ActivatedRoute,
        private taskPopupService: TaskPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
                this.modalRef = this.taskPopupService
                    .openOfficial(TaskOfficialDialogComponent,params);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }

}
