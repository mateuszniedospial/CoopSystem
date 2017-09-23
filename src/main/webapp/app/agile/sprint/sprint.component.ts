import {Component, OnInit, OnDestroy} from "@angular/core";
import {DragulaService} from "ng2-dragula";
import {AgileAbstractComponent} from "../base/agile-abstract.component";
import {Principal} from "../../shared/auth/principal.service";
import {JUserService} from "../../entities/j-user/j-user.service";
import {JUserImgService} from "../../entities/j-user-img/j-user-img.service";
import {JGroupService} from "../../entities/j-group/j-group.service";
import {TaskService} from "../../entities/task/task.service";
import {TaskDescriptionService} from "../../entities/task-description/task-description.service";
import {EventManager} from "ng-jhipster";
import {SprintService} from "../../entities/sprint/sprint.service";
import {Response} from "@angular/http";
import {TaskLifeCycle} from "../../entities/task/task.model";
import {Task} from '../../entities/task/';

@Component({
    selector: 'sprintView',
    templateUrl: './sprint.component.html',
    styleUrls: [
        './sprint.css'
    ]
})
export class SprintComponent extends AgileAbstractComponent implements OnInit, OnDestroy {

    private taskToDo: Task[]=[];
    private taskInProg: Task[]=[];
    private taskDevDone:  Task[]=[];
    private taskInTest:  Task[]=[];
    private taskDone:  Task[]=[];

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
            this.onDropModel(value.slice(1));
        });
    }

    /** This method is not implemented because is abstract **/
    removeAllTasksFromSprint() {}//unused
    createReportView() {}
    destroyData() {}

    ngOnInit(): void {
        super.init('sprint');
        this.registerChangeInSelectedJGroupID();
    }

    ngOnDestroy() {
        this.eventSubscribers.forEach(evS=>{evS.unsubscribe()});
    }

    changeStatusOfTask(statusString:any, status:TaskLifeCycle, taskList:Task[] ){
        let task = this.findTask(statusString, taskList);
        if(task){
            task.lifeCycle = status;
            this.updateTask(task);
            task.lifeCycle=statusString;
        }
    }
    protected onDropModel(args) {
        let destination = args[1].id;

        if(destination=='TODO'){
            this.changeStatusOfTask('TODO', TaskLifeCycle.TODO, this.taskToDo );
        }else if(destination == 'INPROGRESS') {
            this.changeStatusOfTask('INPROGRESS', TaskLifeCycle.INPROGRESS, this.taskInProg );
        } else if(destination =='DEVDONE' ) {
            this.changeStatusOfTask('DEVDONE', TaskLifeCycle.DEVDONE, this.taskDevDone );
        } else if(destination=='TEST') {
            this.changeStatusOfTask('TEST', TaskLifeCycle.TEST, this.taskInTest );
        } else if(destination == 'DONE') {
            this.changeStatusOfTask('DONE', TaskLifeCycle.DONE, this.taskDone);
        }
    }

    private findTask(status: any, taskList:Task[]) {
        let result:Task;
        taskList.forEach(task=>{
            if(task.lifeCycle != status){
                result = task;
            }
        })
        return result;
    }


    initTasks() {
        this.taskInSprint=[];
        this.taskOutOfSprint=[];
        this.taskService.queryTasksBySprintID(this.sprint.id)
            .subscribe(
                (res: Response) => {
                    let response: any[];
                    response = res.json();
                    this.jGroupTasks = response;
                    this.initAvatars();
                    this.taskInSprint = response;
                    this.distriuteTaskToDropModel()
                },
                (res: Response) => {this.onError(res)})
    }


    private distriuteTaskToDropModel() {
        this.taskInSprint.forEach(task=>{
            let lifeCycle : any = task.lifeCycle;
            if(lifeCycle=='TODO'){
                this.taskToDo.push(task);
            }else if(lifeCycle == 'INPROGRESS') {
                this.taskInProg.push(task);
            } else if(lifeCycle=='DEVDONE' || lifeCycle=='REVIEWED') {
                this.taskDevDone.push(task);
            } else if(lifeCycle=='TEST') {
                this.taskInTest.push(task);
            } else if(lifeCycle == 'CLOSED' || lifeCycle == 'DONE' ) {
                this.taskDone.push(task);
            }
        })
    }

    registerChangeInSelectedJGroupID(){
        this.eventSubscribers.push(this.eventManager.subscribe('activeJGroupIDModification', () => {this.sprintDestroyData()}));
    }

    private sprintDestroyData(){
        this.taskToDo = [];
        this.taskInProg = [];
        this.taskDevDone = [];
        this.taskInTest = [];
        this.taskDone = [];
    }
}
