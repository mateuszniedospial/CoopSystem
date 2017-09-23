import {Component, OnInit, OnDestroy} from "@angular/core";
import {DragulaService} from "ng2-dragula";
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
import {Task} from '../../entities/task/';

import {AlertUtil} from "../../shared/util/alert-util";
import {AgileAbstractComponent} from "../base/agile-abstract.component";

/**
 * Created by Master on 21.02.2017.
 */
@Component({
    selector: 'backlog',
    templateUrl: './backlog.component.html',
    styleUrls: [
        './backlog.css'
    ]
})
export class BacklogComponent extends AgileAbstractComponent implements OnInit, OnDestroy{
    public durationTime:number=1;
    private isTaskInit: boolean;
    private isSprintInit:boolean;


    constructor(protected dragulaService : DragulaService,
                protected principal: Principal,
                protected jUserService: JUserService,
                protected jUserImgService: JUserImgService,
                protected jGroupService: JGroupService,
                protected taskService: TaskService,
                protected taskDescriptionService: TaskDescriptionService,
                protected eventManager: EventManager,
                protected sprintService: SprintService){
        super(dragulaService,principal,jUserService,jUserImgService,jGroupService,taskService,taskDescriptionService,eventManager,sprintService);
        dragulaService.dropModel.subscribe((value) => {
            this.onDropModel();
        });
    }

    protected onDropModel() {
        this.taskInSprint.forEach(
            (task: Task) => {
                if (task.sprint === null || task.sprint.id != this.sprint.id) {
                    this.sendToSprintClick(task, true);
                }
            });
        this.taskOutOfSprint.forEach(
            (task: Task) => {
                if (task.sprint) {
                    this.sendToBacklogClick(task, true, true);
                }
            });
    }

    ngOnInit(): void {
        super.init('backlog');
        this.registerSprintAndTasksInit();
        this.registerTaskModificationEventListener();
        this.registerTaskCreatednEventListener();

    }

    ngOnDestroy() {
        this.eventSubscribers.forEach(evS=>evS.unsubscribe());
    }

    createReportView() {}


    //overide
    initTasks() {
        this.cleanTasksArray();
        this.taskService.queryJGroupTasks(this.idOfSelectedGroup)
            .subscribe(
                (res: Response) => {
                    let response: any[];
                    response = res.json();
                    this.jGroupTasks = response;
                    this.eventManager.broadcast({name: 'tasksInit', content: 'OK'});
                    this.initTaskDescription();
                    this.initAvatars();
                },
                (res: Response) => {this.onError(res)})
    }

    private cleanTasksArray() {
        this.taskInSprint = [];
        this.taskOutOfSprint = [];
    }

    sendToBacklogClick(task: Task, isDropUse?: boolean, withClearHistory?: boolean) {
        if (withClearHistory){
            task.historic_sprints = task.historic_sprints.filter(it => it.id != task.sprint.id);
        }
        task.sprint = null;
        this.taskInSprint = this.taskInSprint.filter((it) => it.id != task.id);
        this.updateTask(task);
        if (!isDropUse) {
            this.taskOutOfSprint.push(task);
        }
    }

    sendToSprintClick(task: Task, isDropUse?: boolean) {
        task.sprint = this.sprint;
        if (!task.historic_sprints) {
            task.historic_sprints = [task.sprint];
        } else {
            let existingSprint = task.historic_sprints.find(s => s.id == task.sprint.id);
            if (!existingSprint) {
                task.historic_sprints.push(task.sprint);
            }
        }
        this.taskOutOfSprint = this.taskOutOfSprint.filter((it) => it.id != task.id);
        this.updateTask(task);
        if (!isDropUse) {
            this.taskInSprint.push(task);
        }
    }

    onCreateSprintClick(){
        let sprint :Sprint = new Sprint();
        sprint.jgroup=this.jGroup;
        sprint.sumOfEstimate = 0;
        sprint.sumOfRemaining = 0;
        if(!sprint.durationTime){
            sprint.durationTime = 1;
        }
        sprint.lifeCycle = SprintLifeCycle.FUTURE;
        sprint.title = "Sprint_" + this.jGroup.name;
        this.createSprint(sprint);

    }

    onStartSprintClick(){
        if(this.sprint) {
            this.sprint.startTime = new Date();
            this.sprint.stopTime =  new Date();
            this.sprint.durationTime = this.durationTime;
            this.sprint.stopTime.setDate(this.sprint.stopTime.getDate()+(7*this.durationTime));
            this.sprint.lifeCycle = SprintLifeCycle.ACTIVE;
            this.updateSprint(false);
        } else {
            AlertUtil.createErrorAlert("Try again. Sprint does not exist", this.msgs)
        }
    }

    onStopSprintClick() {
        this.sprint.lifeCycle = SprintLifeCycle.OUTDATED;
        this.sprint.stopTime = new Date();
        this.updateSprint(true);

    }

     removeAllTasksFromSprint() {
        this.taskInSprint.forEach((task: Task) => {
            this.sendToBacklogClick(task, false, false);
        })
    }

    registerTaskModificationEventListener() {

        this.eventSubscribers.push(this.eventManager
            .subscribe('taskModification',
                (response) => this.updateTaskInView(response.content, false)));
    }

    registerTaskCreatednEventListener() {

        this.eventSubscribers.push(this.eventManager
            .subscribe('taskCreated',
                (response) => {
                    this.updateTaskInView(response.content, true)
            }));
    }

    registerSprintAndTasksInit() {
        this.log("sprintAndTaskInit");
        this.eventSubscribers.push(this.eventManager
            .subscribe('activeSprintInit', (response) => {
                this.log(response.content);
                this.isSprintInit = true;
                if (this.isTaskInit) {
                    this.initTaskInSprint();
                    this.log("isTaskInit");
                }
            }));
        this.eventSubscribers.push(this.eventManager
            .subscribe('tasksInit', () => {
                this.isTaskInit = true;
                if (this.isSprintInit ) {
                    this.initTaskInSprint();
                    this.log("isSprintInit");
                }
            }));
    }

    protected updateTaskInView(task:Task, isTaskCreated:boolean) {
        if(isTaskCreated && task.jgroup.id == this.idOfSelectedGroup ){
            this.taskOutOfSprint.push(task);
            this.initAvatarById(task.assignee.id);
        } else {
            let index = this.taskInSprint.findIndex(t=>t.id == task.id);
            this.taskInSprint[index]=task;
            this.initActiveSprint(true);
        }
    }

    addTaskFromPreviousSprint(){
        this.taskService.queryLastSprintTaskByGroupId(this.idOfSelectedGroup)
            .subscribe(
                (res: Response) => {
                    let response: Task[];
                    response = res.json();
                    response.forEach(task => {
                        this.sendToSprintClick(task);
                    });
                    AlertUtil.createSuccessAlert('Tasks added successfuly', this.msgs);
                },
                () => {AlertUtil.createErrorAlert('Try again', this.msgs);}
            )
    }

     generateTitleID(task: Task) {
        let result = "";
        let lc: any = task.lifeCycle;
        if (lc == "DONE" || lc == "CLOSED") {
            result += "<strike>" + task.id + "</strike>" + ": " + task.title
        } else {
            result = task.id + ": " + task.title;
        }
        return result;
    }

    destroyData() {
        this.log("destroy data");
        this.jGroup = null;
        this.sprint = null;
        this.jGroupTasks = [];
        this.avatars = [];
        this.taskInSprint = [];
        this.taskOutOfSprint = [];
        this.msgs = [];
        this.isSprintInit = false;
        this.isTaskInit = false;
    }

    log(message: string) {
        // let date = new Date();
        // console.log(date.getHours() + date.getMinutes() + date.getSeconds() + date.getMilliseconds() + message);

    }

}
