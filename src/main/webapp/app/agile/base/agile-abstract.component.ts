import {DragulaService} from "ng2-dragula";
import {Principal} from "../../shared/auth/principal.service";
import {JUserService} from "../../entities/j-user/j-user.service";
import {JUserImgService} from "../../entities/j-user-img/j-user-img.service";
import {JGroupService} from "../../entities/j-group/j-group.service";
import {TaskService} from "../../entities/task/task.service";
import {TaskDescriptionService} from "../../entities/task-description/task-description.service";
import {EventManager} from "ng-jhipster";
import {DateUtil} from "../../shared/util/date-util";
import {SprintService} from "../../entities/sprint/sprint.service";
import {Response} from "@angular/http";
import {JUser} from "../../entities/j-user/j-user.model";
import {JGroup} from "../../entities/j-group/j-group.model";
import {Sprint} from "../../entities/sprint/sprint.model";
import {Task} from '../../entities/task/';
import {JUserImg} from "../../entities/j-user-img/j-user-img.model";
import {Subscription} from "rxjs";
import {Message} from "primeng/components/common/api";
import {AlertUtil} from "../../shared/util/alert-util";
import {JGroupStorageComponent} from "../../shared/groupLocalStorage/jgroup-storage.component";
import {TaskIconUtil} from "../../shared/util/task-icon-util";
import {AgileBase} from "./agile-base";

export abstract class AgileAbstractComponent extends AgileBase {
    public activJUser: JUser;
    public idOfSelectedGroup;
    public jGroup:JGroup;
    public jGroupTasks : Task[]=[];
    public avatars : JUserImg[]=[];
    public taskInSprint: Task[]=[];
    public taskOutOfSprint : Task[]=[];
    protected eventSubscribers: Subscription[] = [];
    private activeSprintExist: boolean = true;
    private childInstanceName:string;


    constructor(protected dragulaService : DragulaService,
                protected principal: Principal,
                protected jUserService: JUserService,
                protected jUserImgService: JUserImgService,
                protected jGroupService: JGroupService,
                protected taskService: TaskService,
                protected taskDescriptionService: TaskDescriptionService,
                protected eventManager: EventManager,
                protected sprintService: SprintService){
        super(sprintService, jUserService, jGroupService);
    }

    //overide me
    abstract initTasks() ;
    abstract removeAllTasksFromSprint();
    abstract createReportView();

    init(instance: string): void {
        this.childInstanceName = instance;
        this.principal.identity().then((account) => {
            this.initJUserIDByJhiId(account.id);
            this.idOfSelectedGroup = JGroupStorageComponent.retrieveActiveJGroupID();
            this.initJGroupById(this.idOfSelectedGroup);
            this.initActiveSprint();
            if(this.childInstanceName=='backlog')
                this.initTasks();
            this.registerChangeInActiveJGroupID();
        });
    }

    private registerChangeInActiveJGroupID(){
        this.eventSubscribers.push(this.eventManager.subscribe('activeJGroupIDModification', () => {
            this.initReloadData()}));
    }

    initReloadData(){
        this.destroyData();
        this.principal.identity().then(() => {
            this.idOfSelectedGroup = JGroupStorageComponent.retrieveActiveJGroupID();
            this.initJGroupById(this.idOfSelectedGroup);
            this.initActiveSprint();
            if(this.childInstanceName=='backlog') {
                this.initTasks();
            }
        });
    }

    protected initActiveSprint(withoutEvent?:boolean) {
        this.sprintService.queryByJGroupId({
            'query': this.idOfSelectedGroup
        }).subscribe(
            (res: Response) => {
                let response: any[];
                response = res.json();
                this.activeSprintExist = response.length>0;
                this.sprint = response[0];
                if(!withoutEvent){
                    this.eventManager.broadcast({name: 'activeSprintInit', content: this.childInstanceName});
                    if(this.childInstanceName ==='report')
                        this.createReportView();
                }
                if(this.childInstanceName=='sprint')
                    this.initTasks();
            },
            (res: Response) => {
                this.onError(res)})
    }

    protected initTaskDescription() {
        this.jGroupTasks.forEach((task)=>{
            this.taskDescriptionService.queryByTaskId({"query" : task.id})
                .subscribe(
                    (res:Response)=>{
                        task["description"] = res.json().content;},
                    (res: Response) => {this.onError(res)})
                })
    }

    public getAvatarById(jId:number): JUserImg{
        let result = this.avatars.find((it:JUserImg)=>it.juser.id == jId);
        if(result){
            return result;
        }
    }

    avatarRequest:number[]=[];
    protected initAvatars() {
        this.jGroupTasks.forEach((task:Task)=>{
            this.initAvatarById(task.assignee.id);
        })
    }

    protected initAvatarById(id : number) {
        if (!this.avatarRequest.find((it) => it == id)) {
            this.jUserImgService.queryJImgOfJUser(id)
                .subscribe(
                    (res: Response) => {
                        this.avatars.push(res);},
                    (res: Response) => {
                        this.onError(res)});
            this.avatarRequest.push(id);
        }
    }

    protected initTaskInSprint() {
        this.jGroupTasks.forEach((task: Task) => {
            if (task.sprint) {
                if (task.sprint.id == this.sprint.id) {
                    this.taskInSprint.push(task);
                } else {
                    this.taskOutOfSprint.push(task);
                }
            } else {
                this.taskOutOfSprint.push(task);
            }
        })
    }

    public  extractNumberFromEstimate(input: string) {
        let result;
        result = input.replace(/[^0-9]/g, '');
        if (input == "?")
            return "?";
        return result
    }

    public  getTypeIcon(type:string){
        return TaskIconUtil.getTypeIcon(type);
    }

     formatDate(input: any) {
        return DateUtil.formatDate(input);
     }

    updateSprint(withRemoveTasksFromSprint : boolean){
        this.sprint.jgroup = this.jGroup;
        super.updateSprint(this.sprint, ()=>{
            if(withRemoveTasksFromSprint)
                this.removeAllTasksFromSprint();
        });
    }

    updateTask(task:Task) {
        task.juser=this.activJUser;
        this.taskService.update(task).subscribe(
            () => {
                if(this.childInstanceName=='backlog')
                    this.initActiveSprint(true);
                AlertUtil.createSuccessAlert('Task updated successfuly', this.msgs);
            },
            () => { AlertUtil.createErrorAlert('Try again', this.msgs)});
    }

     getSign(numberOfSign:number, input:string) {
        return input.substr(0, numberOfSign);
    }

     abstract destroyData()
}
