import {Component, OnInit, OnDestroy} from "@angular/core";
import {Principal} from "../../shared/auth/principal.service";
import {JUserService} from "../../entities/j-user/j-user.service";
import {JUserImgService} from "../../entities/j-user-img/j-user-img.service";
import {JGroupService} from "../../entities/j-group/j-group.service";
import {TaskService} from "../../entities/task/task.service";
import {TaskDescriptionService} from "../../entities/task-description/task-description.service";
import {EventManager} from "ng-jhipster";
import {SprintService} from "../../entities/sprint/sprint.service";
import {Response} from "@angular/http";
import {Sprint, SprintLifeCycle} from "../../entities/sprint/sprint.model";

import {ActivatedRoute} from "@angular/router";
import {JGroupStorageComponent} from "../../shared/groupLocalStorage/jgroup-storage.component";
import {Subscription} from "rxjs";
import {Project} from "../../entities/project/project.model";
import {JUser} from "../../entities/j-user/j-user.model";
import {AgileBase} from "../base/agile-base";
import {AlertUtil} from "../../shared/util/alert-util";
import {ProjectService} from "../../entities/project/project.service";

/**
 * Created by Master on 21.02.2017.
 */
@Component({
    selector: 'sprint-profile',
    templateUrl: './profile.component.html',
    styleUrls: [
        './profile.css'
    ]
})
export class SprintProfileComponent extends AgileBase implements OnInit, OnDestroy {
    private subscription: any;
    private sprintId: number;
    private sprintExist: boolean = true;
    private idOfSelectedGroup: string;
    private duration: any = 1;
    private showDuration: boolean;
    protected eventSubscribers: Subscription[] = [];
    private relatedProjects: Project[];

    constructor(protected principal: Principal,
                private route: ActivatedRoute,
                protected jUserService: JUserService,
                protected jUserImgService: JUserImgService,
                protected jGroupService: JGroupService,
                protected taskService: TaskService,
                protected taskDescriptionService: TaskDescriptionService,
                protected eventManager: EventManager,
                protected sprintService: SprintService,
                private projectService: ProjectService) {
        super(sprintService, jUserService, jGroupService);
    }

    ngOnInit(): void {
        this.subscription = this.route.params.subscribe(params => {
            this.principal.identity().then((account) => {
                this.initJUserIDByJhiId(account.id);
                this.sprintId = params['id'];
                this.initProfile();

            })
        });
    }

    private initProfile() {
        if (this.sprintId == 0) {
            this.idOfSelectedGroup = JGroupStorageComponent.retrieveActiveJGroupID();
            this.initJGroupById(this.idOfSelectedGroup);
            this.initRelatedProject(this.idOfSelectedGroup);
            this.initActiveSprint();
            this.registerChangeInActiveJGroupID();
        } else {
            this.sprintService.find(this.sprintId).subscribe(
                (res: Response) => {
                    this.sprint = res;
                    this.initEpmtyField()
                    this.initJGroupById(this.sprint.jgroup.id);
                    this.initRelatedProject(this.sprint.jgroup.id);
                },
                () => {this.onError()});
        }
    }

    ngOnDestroy() {
        this.eventSubscribers.forEach(it=>{it.unsubscribe()});
    }

    saveMethod(obj) {
        if (obj.description) {
            this.sprint.description = obj.description;
        } else if (obj.retrospective) {
            this.sprint.retrospective = obj.retrospective;
        } else if (obj.title) {
            this.sprint.title = obj.title;
        } else if (obj.duration) {
            this.duration = obj.duration;
            this.sprint.durationTime = obj.duration;
            this.sprint.stopTime = new Date(this.sprint.startTime.getTime());
            this.sprint.stopTime.setDate(this.sprint.startTime.getDate() + (7 * this.sprint.durationTime));
        }
        this.updateSprint(this.sprint, () => {});
        this.showDuration = false;
    }


    checkPermissions() {
        if (this.jGroup && this.activJUser) {
            let hasPermission: boolean = false;
            let jusers: JUser[] = this.jGroup.jusers;
            jusers.forEach((it) => {
                if (it.id == this.activJUser.id) {
                    hasPermission = true;
                }});
            return hasPermission;
        }
        return false;
    }

    onStopSprintClick() {
        this.runIfHasPermission(() => {
            let lifeCycle: any = this.sprint.lifeCycle;
            if (lifeCycle == "ACTIVE") {
                this.sprint.lifeCycle = SprintLifeCycle.OUTDATED;
                this.sprint.stopTime = new Date();
                this.updateSprint(this.sprint, () => {
                });
            } else {
                AlertUtil.createErrorAlert("Sprint is not active. Operation denied.", this.msgs);
            }
        })
    }

    onChangeDurationClick() {
        this.runIfHasPermission(() => {
            let lifeCycle: any = this.sprint.lifeCycle;
            if (lifeCycle == "ACTIVE") {
                this.showDuration = true;
            } else {
                AlertUtil.createErrorAlert("Sprint is not active. Operation denied.", this.msgs);
            }
        })
    }

    onCreateSprintClick() {
        this.runIfHasPermission(() => {
            let sprint :Sprint = new Sprint();
            sprint.jgroup=this.jGroup;
            sprint.sumOfEstimate = 0;
            sprint.sumOfRemaining = 0;
            if(!sprint.durationTime){
                sprint.durationTime = 1;
            }
            sprint.lifeCycle = SprintLifeCycle.FUTURE;
            sprint.title = "Sprint_" + this.jGroup.name;
            this.createSprint(sprint, ()=>{ this.initEpmtyField() });
        })
    }

    runIfHasPermission(method: any) {
        if (this.checkPermissions()) {
            method()
        } else {
            AlertUtil.createErrorAlert('You have not permission.', this.msgs)
        }
    }

    protected initActiveSprint() {
        this.sprintService.queryByJGroupId({
            'query': this.idOfSelectedGroup
        }).subscribe(
            (res: Response) => {
                let response: any[];
                response = res.json();
                if (response.length == 0) {
                    this.sprintExist = false;
                } else {
                    this.sprint = response[0];
                    this.initEpmtyField()
                }
            },
            (res: Response) => {
                this.onError(res)
            })
    }


    private initEpmtyField() {
        if (this.sprint.description == null) {
            this.sprint.description = "Here is a place where you can place the <b>definition of done, the definition of ready, sprint rules</b> and many others. Click me to modified...";
        }
        if (this.sprint.retrospective == null) {
            this.sprint.retrospective = "Here is a place to write your tips about following sprint. You can write <b>advantages</b> and <b>disadvantages</b> this sprint. All that is here written will be <b>anonymous</b>. Click me to modify...";
        }
    }

    private registerChangeInActiveJGroupID(){
        this.eventSubscribers.push(this.eventManager.subscribe('activeJGroupIDModification', () => {
            this.destroyData();
            this.initProfile()}));
    }

    private destroyData() {
        this.sprintExist = false;
        this.showDuration = false;
        this.sprint = null;
        this.jGroup = null;
    }

    private initRelatedProject(groupId: any) {
        this.projectService.queryByGroupId(groupId)
            .subscribe((res:Response)=>{
                let response : Project[] = res.json();
                this.relatedProjects = response;
            },(res:Response)=>{})
    }
}
