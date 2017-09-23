import {OnInit, OnDestroy, Component} from "@angular/core";
import {Project} from "./project.model";
import {JUser} from "../j-user/j-user.model";
import {Task, TaskLifeCycle} from "../task/task.model";
import {TaskService} from "../task/task.service";
import {ActivatedRoute} from "@angular/router";
import {Principal} from "../../shared/auth/principal.service";
import {EventManager} from "ng-jhipster";
import {ProjectService} from "./project.service";
import {JUserService} from "../j-user/j-user.service";
import {WorkLog} from "../work-log/work-log.model";
import {WorkLogService} from "../work-log/work-log.service";
import {Subscription} from "rxjs";
/**
 * Created by Chrono on 22.06.2017.
 */
@Component({
    selector: 'project-report',
    templateUrl: './project-report.component.html',
})
export class ProjectReportComponent implements OnInit, OnDestroy {

    project: Project;
    activeJUser: JUser;

    tasksWithinProject: Task[] = [];

    tasksDone: Task[] = [];
    tasksToDo: Task[] = [];
    tasksInProgress: Task[] = [];
    tasksInTest: Task[] = [];
    tasksClosed: Task[] = [];

    sumToDoRemaining: number = 0;
    sumToDoEstimated: number = 0;

    sumInProgressRemaining: number = 0;
    sumInProgressEstimated: number = 0;

    sumInTestRemaining: number = 0;
    sumInTestEstimated: number = 0;

    sumDoneEstimated: number = 0;
    sumClosedEstimated: number = 0;

    sumDoneTimeSpent: number = 0;
    sumClosedTimeSpent: number = 0;

    sumToDoTimeSpent: number = 0;
    sumInProgressTimeSpent: number = 0;
    sumInTestTimeSpent: number = 0;

    doneWorkLogs: WorkLog[] = [];
    toDoWorkLogs: WorkLog[] = [];
    inProgressWorkLogs: WorkLog[] = [];
    inTestWorkLogs: WorkLog[] = [];
    closedWorkLogs: WorkLog[] = [];

    donePercents: number;
    toDoPercents: number;
    inProgressPercents: number;
    inTestPercents: number;
    closedPercents: number;

    finishedTasks:number = 0;

    remainingTimeOfUndone: number;
    estimateTimeOfUndone: number;

    sumOfEstimateTime: number;

    subscription: any;
    eventSubscriber: Subscription;

    constructor(
        private taskService: TaskService,
        private projectService: ProjectService,
        private jUserService: JUserService,
        private workLogService: WorkLogService,
        private route: ActivatedRoute,
        private eventManager: EventManager,
        private principal: Principal,
    ) {
    }

    ngOnInit(): void {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });

        this.registerEvents();
    }

    private load(id) {
        this.projectService.find(id).subscribe(project => {
            this.project = project;
            this.findAllProjectsTasks(project.id);
            this.queryActiveJUser();
        });
    }

    private findAllProjectsTasks(id) {
        this.taskService.findProjectsTasks(id).subscribe(res => {
            this.tasksWithinProject = res.json();
            this.divideTasks();
        });
    }

    private queryActiveJUser() {
        this.principal.identity().then((user) => {
            this.jUserService.queryJUser(user.id).subscribe(jUser => {
                this.activeJUser = jUser;
            });
        });
    }

    private divideTasks() {
        for(let task of this.tasksWithinProject){
            if(task.lifeCycle.toString() == "TODO"){
                this.tasksToDo.push(task);
            } else if(task.lifeCycle.toString() == "INPROGRESS" || task.lifeCycle.toString() == "DEVDONE" || task.lifeCycle.toString() == "REVIEWED"){
                this.tasksInProgress.push(task);
            } else if(task.lifeCycle.toString() == "TEST"){
                this.tasksInTest.push(task);
            } else if(task.lifeCycle.toString() == "DONE"){
                this.tasksDone.push(task);
            } else if(task.lifeCycle.toString() == "CLOSED"){
                this.tasksClosed.push(task);
            }
        }
        this.calculatePercents();
        this.calculateTimes();
        this.loadWorkLogs();
    }

    calculatePercents(){
        this.donePercents = this.percent(this.tasksDone.length);
        this.toDoPercents = this.percent(this.tasksToDo.length);
        this.inProgressPercents = this.percent(this.tasksInProgress.length);
        this.inTestPercents = this.percent(this.tasksInTest.length);
        this.closedPercents = this.percent(this.tasksClosed.length);
    }

    percent(howMany:number): number{
        return Number.parseFloat(((howMany*100)/this.tasksWithinProject.length).toFixed(2));
    }

    private calculateTimes() {
        let remainingTimeOfUndone:number = 0;
        let estimateTimeOfUndone:number = 0;

        for(let task of this.tasksToDo){
            remainingTimeOfUndone += Number.parseFloat(task.remainingTime);
            estimateTimeOfUndone += Number.parseFloat(task.estimateTime);

            this.sumToDoRemaining += Number.parseFloat(task.remainingTime);
            this.sumToDoEstimated += Number.parseFloat(task.estimateTime);
        }

        for(let task of this.tasksInProgress){
            remainingTimeOfUndone += Number.parseFloat(task.remainingTime);
            estimateTimeOfUndone += Number.parseFloat(task.estimateTime);

            this.sumInProgressRemaining += Number.parseFloat(task.remainingTime);
            this.sumInProgressEstimated += Number.parseFloat(task.estimateTime);
        }

        for(let task of this.tasksInTest){
            remainingTimeOfUndone += Number.parseFloat(task.remainingTime);
            estimateTimeOfUndone += Number.parseFloat(task.estimateTime);

            this.sumInTestRemaining += Number.parseFloat(task.remainingTime);
            this.sumInTestEstimated += Number.parseFloat(task.estimateTime);
        }

        for(let task of this.tasksDone){
            this.sumDoneEstimated += Number.parseFloat(task.estimateTime);
        }

        for(let task of this.tasksClosed){
            this.sumClosedEstimated += Number.parseFloat(task.estimateTime);
        }

        this.remainingTimeOfUndone = remainingTimeOfUndone;
        this.estimateTimeOfUndone = estimateTimeOfUndone;

        let sumOfEstimateTime:number = 0;

        for(let task of this.tasksWithinProject){
            sumOfEstimateTime += Number.parseFloat(task.estimateTime);
        }

        this.sumOfEstimateTime = sumOfEstimateTime;

        this.finishedTasks = this.tasksDone.length + this.tasksClosed.length;
    }

    private loadWorkLogs() {
        for(let task of this.tasksToDo){
            this.workLogService.queryByTaskId({'query': task.id}).subscribe( res => {
                for(let worklog of res.json()){
                    this.toDoWorkLogs.push(worklog);
                    this.sumToDoTimeSpent += (worklog.timeInHour)/8;
                }
            })
        }
        for(let task of this.tasksInProgress){
            this.workLogService.queryByTaskId({'query': task.id}).subscribe( res => {
                for(let worklog of res.json()){
                    this.inProgressWorkLogs.push(worklog);
                    this.sumInProgressTimeSpent += (worklog.timeInHour)/8;
                }
            })
        }
        for(let task of this.tasksInTest){
            this.workLogService.queryByTaskId({'query': task.id}).subscribe( res => {
                for(let worklog of res.json()){
                    this.inTestWorkLogs.push(worklog);
                    this.sumInTestTimeSpent += (worklog.timeInHour)/8;
                }
            })
        }
        for(let task of this.tasksDone){
            this.workLogService.queryByTaskId({'query': task.id}).subscribe( res => {
                for(let worklog of res.json()){
                    this.doneWorkLogs.push(worklog);
                    this.sumDoneTimeSpent += (worklog.timeInHour)/8;
                }
            })
        }
        for(let task of this.tasksClosed){
            this.workLogService.queryByTaskId({'query': task.id}).subscribe( res => {
                for(let worklog of res.json()){
                    this.closedWorkLogs.push(worklog);
                    this.sumClosedTimeSpent += (worklog.timeInHour)/8;
                }
            })
        }
    }

    private registerEvents() {
        this.registerChangesInTasks();
        this.registerChangesInWorkLogs();
    }

    registerChangesInTasks() {
        this.eventSubscriber = this.eventManager.subscribe('taskListModification', (response) =>{this.findAllProjectsTasks(this.project.id);});
    }

    registerChangesInWorkLogs(){
        this.eventSubscriber = this.eventManager.subscribe('workLogListModification', (response) =>{this.loadWorkLogs();});
    }

    ngOnDestroy(): void {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }
}
