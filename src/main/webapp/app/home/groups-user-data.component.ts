import {OnInit, Component, OnDestroy} from "@angular/core";
import {JGroup} from "../entities/j-group/j-group.model";
import {JGroupService} from "../entities/j-group/j-group.service";
import {Principal} from "../shared/auth/principal.service";
import {Response} from "@angular/http";
import {AlertService, EventManager} from "ng-jhipster";
import {JUserService} from "../entities/j-user/j-user.service";
import {TaskService} from "../entities/task/task.service";
import {JUser} from "../entities/j-user/j-user.model";
import {Task} from "../entities/task/task.model";
import {JGroupImg} from "../entities/j-group-img/j-group-img.model";
import {JUserImgService} from "../entities/j-user-img/j-user-img.service";
import {JUserImg} from "../entities/j-user-img/j-user-img.model";
import {JGroupImgService} from "../entities/j-group-img/j-group-img.service";
import {TaskDescriptionService} from "../entities/task-description/task-description.service";
import {TaskDescription} from "../entities/task-description/task-description.model";
import {Subscription} from "rxjs";
import {DateUtil} from "../shared/util/date-util";
import {TaskIconUtil} from "../shared/util/task-icon-util";
/**
 * Created by Mateusz Niedospial on 12.03.2017.
 */

@Component({
    selector: 'groups-user-data',
    templateUrl: 'groups-user-data.component.html',
    styleUrls: [
        'groups-user-data.css'
    ]
})
export class GroupsUserDataComponent implements OnInit, OnDestroy {
    /**Static reference using in view .html */
    public taskIconStaticRef : TaskIconUtil = TaskIconUtil;
    public dateUtilStaticRef : DateUtil = DateUtil;
    /**end of static*/


    private jUser: JUser;
    private jUserImg: JUserImg;
    private jGroups: JGroup[];
    private theFirstJGroupFound: JGroup;
    private theRestJGroupFound: JGroup[];
    private jGroupsImgMap:{[key:string]:JGroupImg;} = {};
    private tasksMap:{[key:string]:Task[];} = {};
    private jUserTasks: Task[];
    private jUserTasksDescriptions:{[key:string]: TaskDescription;} = {};

    eventSubscriber: Subscription;

    constructor(private principal: Principal,
                private alertService: AlertService,
                private jUserService: JUserService,
                private jUserImgService: JUserImgService,
                private jGroupService: JGroupService,
                private jGroupImgService: JGroupImgService,
                private taskService: TaskService,
                private taskDescriptionService: TaskDescriptionService,
                private eventManager: EventManager) {
    }

    ngOnInit() {
        this.initializeDataGathering();
        this.registerEvents();
    }

    private registerEvents() {
        this.registerChangeInTasks();
        this.registerChangeInJGroups();
    }

    initializeDataGathering(){
        this.principal.identity().then((user) => {
            this.jUserService.queryJUser(user.id).subscribe(jUser => {
                this.jUser = jUser;
                this.queryJGroupsData(jUser.id);
                this.queryJImgOfJUser(jUser.id);
                this.queryTasksAssignedToJUser(jUser.id);
            });
        });
    }

    queryJGroupsData(id) {
        this.jGroupService.querySpecificUserGroups(id).subscribe((res: Response) => {
                this.jGroups = res.json();
                this.theFirstJGroupFound = this.jGroups[0];
                this.theRestJGroupFound = this.jGroups.slice(1, (res.json().len));

                for(let jGroup of this.jGroups){
                    this.queryTasks(jGroup.id);
                    this.queryJGroupImgs(jGroup.id);
                }
            },
            (res: Response) => this.onError(res.json())
        );
    }

    queryJImgOfJUser(id){
        this.jUserImgService.queryJImgOfJUser(id).subscribe(jUserImg => {
            this.jUserImg = jUserImg;
        });
    }

    queryTasks(id) {
        this.taskService.queryJGroupTasks(id).subscribe((res: Response) => {
                this.tasksMap[id] = res.json();
            },
            (res: Response) => this.onError(res.json())
        );
    }

    private initJGroupsTasksReload(){
        for(let jGroup of this.jGroups){
            this.queryTasks(jGroup.id);
        }
        this.initTasksAssignedToUserReload();
    }

    private initTasksAssignedToUserReload(){
        this.principal.identity().then((user) => {
            this.jUserService.queryJUser(user.id).subscribe(jUser => {
                this.queryTasksAssignedToJUser(jUser.id);
            });
        });
    }

    private initJGroupsReload(){
        this.principal.identity().then((user) => {
            this.jUserService.queryJUser(user.id).subscribe(jUser => {
                this.queryJGroupsData(jUser.id);
            });
        });
    }

    queryTasksAssignedToJUser(id){
        this.taskService.queryJUserTasks(id).subscribe((res: Response) => {
            this.jUserTasks = res.json();

            for(let task of this.jUserTasks){
                this.queryJUserTaskDescription(task.id);
            }

        },
            (res: Response) => this.onError(res.json())
        );
    }

    queryJUserTaskDescription(id){
        this.taskDescriptionService.queryTaskDescriptionOfSpecificTask(id).subscribe( taskDescription => {
            this.jUserTasksDescriptions[id] = taskDescription;
        });
    }

    queryJGroupImgs(id) {
        this.jGroupImgService.queryJImg(id).subscribe(jGroupImg => {
            this.jGroupsImgMap[id] = jGroupImg;
        });
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    registerChangeInTasks(){
        this.eventSubscriber = this.eventManager.subscribe('taskListModification', (response) => {
            this.initJGroupsTasksReload()
        });
    }

    registerChangeInJGroups(){
        this.eventSubscriber = this.eventManager.subscribe('jGroupListModification', (response) => {
            this.initJGroupsReload()
        });
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }
}
