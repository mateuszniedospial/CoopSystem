/**
 * Created by Chrono on 03.05.2017.
 */
import {Component, OnInit, OnDestroy} from "@angular/core";
import {JGroup} from "./j-group.model";
import {ActivatedRoute} from "@angular/router";
import {JGroupService} from "./j-group.service";
import {Task} from "../task/task.model";
import {JGroupImg} from "../j-group-img/j-group-img.model";
import {JGroupImgService} from "../j-group-img/j-group-img.service";
import {TaskService} from "../task/task.service";
import {Response} from "@angular/http";
import {AlertService, EventManager} from "ng-jhipster";
import {Principal} from "../../shared/auth/principal.service";
import {JUserService} from "../j-user/j-user.service";
import {DataUtils} from 'ng-jhipster';
import {TaskHistoryService} from "../task-history/task-history.service";
import {TaskHistory} from "../task-history/task-history.model";
import {Subscription} from "rxjs";
import {Message} from "primeng/components/common/api";
import {JUser} from "../j-user/j-user.model";
import {DateUtil} from "../../shared/util/date-util";
import {TaskIconUtil} from "../../shared/util/task-icon-util";
import {JoinJGroupRequestService} from "../join-j-group-request/join-j-group-request.service";
import {JoinJGroupRequest} from "../join-j-group-request/join-j-group-request.model";
@Component({
    selector: 'j-group-profile',
    templateUrl: './j-group-profile.component.html',
    styleUrls: [
        'j-group-profile.css'
    ]
})
export class JGroupProfileComponent implements OnInit, OnDestroy{
    /**Static reference using in view .html */
    public dateUtilStaticRef : DateUtil = DateUtil;
    public taskIconStaticRef : TaskIconUtil = TaskIconUtil;
    /**end of static*/

    private loggedInJUserId;
    private activeJUser: JUser;
    private jGroup: JGroup;
    private jGroupImg: JGroupImg;
    private tasks: Task[];
    private tasksHistories: TaskHistory[];

    private joinRequests: JoinJGroupRequest[];

    private doesBelong:boolean = false;
    private didSendRequest:boolean = false;

    private subscription: any;
    eventSubscriber: Subscription;

    msgs: Message[] = [];

    constructor(
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private dataUtils: DataUtils,
        private alertService: AlertService,
        private jUserService: JUserService,
        private jGroupService: JGroupService,
        private joinJGroupRequestService: JoinJGroupRequestService,
        private jGroupImgService: JGroupImgService,
        private taskService: TaskService,
        private taskHistoryService: TaskHistoryService,
        private eventManager: EventManager
    ){}

    ngOnInit() {
        this.principal.identity().then((user) => {
            this.jUserService.queryJUser(user.id).subscribe(jUser => {
                this.loggedInJUserId = jUser.id;
                this.activeJUser = jUser;
                this.subscription = this.activatedRoute.params.subscribe(params => {
                    this.initializeDataGathering(params['id']);
                });
            });
        });
        this.registerEvents();
    }

    private registerEvents(){
        this.registerChangeInTasks();
        this.registerChangeInJGroups();
        this.registerChangeInTaskHistories();
        this.registerChangeInJUsers();
        this.registerChangeInRequests();
        this.registerChangeInJGroupImg();
        this.registerChangeInJGroupsByLeave();
    }

    private initializeDataGathering(id) {
        this.jGroupService.find(id).subscribe(jGroup => {
            this.jGroup = jGroup;
            this.queryJGroupImg(jGroup.id);
            this.queryTasks(jGroup.id);
            this.queryTasksHistories(jGroup.id);
            this.checkIfBelongsToGroup();
            this.queryRequests(jGroup.id);
        });
    }

    private queryJGroupImg(id){
            this.jGroupImgService.queryJImg(id).subscribe(jGroupImg => {
                this.jGroupImg = jGroupImg;
            })
    }

    private queryTasks(id){
        this.taskService.queryJGroupTasks(id).subscribe((res: Response) => {
                this.tasks = res.json();
            },
            (res: Response) => this.onError(res.json())
        );
    }

    private queryTasksHistories(id){
       this.taskHistoryService.queryHistoriesOfJGroupTasks(id).subscribe((res: Response) => {
           this.tasksHistories = res.json();
       },
           (res: Response) => this.onError(res.json())
       );
    }

    private queryRequests(id){
        this.joinJGroupRequestService.querySpecificGroupRequests(id).subscribe( res => {
            this.joinRequests = res.json();
            this.checkIfSentRequest();
        })
    }

    reloadRequests(){
        this.joinJGroupRequestService.querySpecificGroupRequests(this.jGroup.id).subscribe( res => {
            this.joinRequests = res.json();
            this.checkIfSentRequest();
        })
    }

    private checkIfBelongsToGroup(){
        for(let jUser of this.jGroup.jusers){
            if(jUser.id == this.activeJUser.id){
                this.doesBelong = true;
            }
        }
    }

    private checkIfSentRequest(){
        for(let request of this.joinRequests){
            if(request.whoRequested.id == this.activeJUser.id){
                this.didSendRequest = true;
            }
        }
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    private initJGroupsTasksReload(){
        this.queryTasks(this.jGroup.id);
    }

    private initJGroupReload(){
        this.subscription = this.activatedRoute.params.subscribe(params => {
            this.jGroupService.find(params['id']).subscribe(jGroup => {
                this.jGroup = jGroup;
                this.queryJGroupImg(jGroup.id);
            });
        });
    }

    private initTasksHistoriesReload(){
        this.queryTasksHistories(this.jGroup.id);
    }

    checkPermissions() {
        if(this.activeJUser && this.jGroup){
            let find = this.jGroup.jusers.find(it=> it.id == this.activeJUser.id);
            if(find) return true;
            else return false;
        }
        return false;
    }

    registerChangeInTasks(){
        this.eventSubscriber = this.eventManager.subscribe('taskListModification', (response) => {
            this.initJGroupsTasksReload();
        });
    }

    registerChangeInJGroups(){
        this.eventSubscriber = this.eventManager.subscribe('jGroupListModification', (response) => {
            this.initJGroupReload();
        });
    }

    registerChangeInJGroupsByLeave(){
        this.eventSubscriber = this.eventManager.subscribe('jGroupListModificationByLeave', (response) => {
            this.doesBelong = false;
            this.initJGroupReload();
        });
    }

    registerChangeInTaskHistories(){
        this.eventSubscriber = this.eventManager.subscribe('taskHistoryModification', (response) => {
            this.initTasksHistoriesReload();
        });
    }

    registerChangeInJUsers() {
        this.eventSubscriber = this.eventManager.subscribe('jUserListModification', (response) => this.ngOnInit());
    }

    registerChangeInRequests(){
        this.eventSubscriber = this.eventManager.subscribe('joinJGroupRequestListModification', (response) => {
            this.reloadRequests();
        });
    }

    registerChangeInJGroupImg(){
        this.eventSubscriber = this.eventManager.subscribe('jGroupImgListModification', (response) => {
            this.queryJGroupImg(this.jGroup.id);
        })
    }

    saveMethod(obj) {
        if (obj.name){
            this.jGroup.name = obj.name;
            this.jGroupService.update(this.jGroup)
                .subscribe(
                    () => {
                        this.createSuccessAlert('Your changes have been saved successfully!')
                        this.eventManager.broadcast({name: 'jGroupListModification', content: 'UPDATED'})
                    },
                    () => {
                        this.createErrorAlert('Your changes have not been saved due to some error.')
                    });
        } else if (obj.description){
            this.jGroup.description = obj.description;
            this.jGroupService.update(this.jGroup)
                .subscribe(
                    () => {
                        this.createSuccessAlert('Your changes have been saved successfully!')
                        this.eventManager.broadcast({name: 'jGroupListModification', content: 'UPDATED'})
                    },
                    () => {
                        this.createErrorAlert('Your changes have not been saved due to some error.')
                    });
        }
    }

    createSuccessAlert(message: string) {
        this.msgs.push({severity:'success', summary:'Success', detail:message});
        setTimeout(()=>{
            this.msgs=[];
        },2000);
    }

    createErrorAlert(message: string) {
        this.msgs.push({severity:'error', summary:'Error', detail:message});
        setTimeout(()=>{
            this.msgs=[];
        },2000);
    }

    ngOnDestroy(){
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }
}
