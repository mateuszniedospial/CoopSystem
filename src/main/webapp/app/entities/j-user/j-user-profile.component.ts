import {Component, OnDestroy, OnInit} from "@angular/core";
import {JUser} from "./j-user.model";
import {Principal} from "../../shared/auth/principal.service";
import {JUserService} from "./j-user.service";
import {JUserImgService} from "../j-user-img/j-user-img.service";
import {JGroupService} from "../j-group/j-group.service";
import {EventManager, AlertService} from "ng-jhipster";
import {JUserImg} from "../j-user-img/j-user-img.model";
import {Response} from "@angular/http";
import {JGroup} from "../j-group/j-group.model";
import {Task} from "../task/task.model";
import {TaskService} from "../task/task.service";
import {Subscription} from "rxjs";
import {TaskHistory} from "../task-history/task-history.model";
import {TaskHistoryService} from "../task-history/task-history.service";
import {Message} from "primeng/components/common/api";
import {JGroupImg} from "../j-group-img/j-group-img.model";
import {JGroupImgService} from "../j-group-img/j-group-img.service";
import {TaskDescription} from "../task-description/task-description.model";
import {TaskDescriptionService} from "../task-description/task-description.service";
import {AccountService} from "../../shared/auth/account.service";
import {UserService} from "../../shared/user/user.service";
import {ActivatedRoute} from "@angular/router";
import {AlertUtil} from "../../shared/util/alert-util";
import {DateUtil} from "../../shared/util/date-util";
import {TaskIconUtil} from "../../shared/util/task-icon-util";
/**
 * Created by Chrono on 08.04.2017.
 */

@Component({
    selector: 'j-user-profile',
    templateUrl: './j-user-profile.component.html',
    styleUrls: [
        'j-user-profile.css'
    ]
})
export class JUserProfileComponent implements OnInit, OnDestroy {
    /**Static reference using in view .html */
    public dateUtilStaticRef : DateUtil = DateUtil;
    public taskIconStaticRef : TaskIconUtil = TaskIconUtil;
    /**end of static*/

    private userAccount: any;
    private jUser: JUser;
    private jUserImg: JUserImg;
    private jGroups: JGroup[];
    private jGroupsImgMap:{[key:string]:JGroupImg;} = {};
    private tasksMap:{[key:string]: Task[];} = {};
    private tasksHistories: TaskHistory[];
    private jUserTasks: Task[];
    private jUserTasksDescriptions:{[key:string]: TaskDescription;} = {};
    private id: number;
    subscription: any;

    activeJUser: JUser;
    msgs: Message[] = [];
    eventSubscriber: Subscription;

    constructor(private principal: Principal,
                private alertService: AlertService,
                private accountService: AccountService,
                private jUserService: JUserService,
                private userService: UserService,
                private jUserImgService: JUserImgService,
                private jGroupService: JGroupService,
                private jGroupImgService: JGroupImgService,
                private taskService: TaskService,
                private taskHistoryService: TaskHistoryService,
                private taskDescriptionService: TaskDescriptionService,
                private eventManager: EventManager,
                private route: ActivatedRoute){
    }


    ngOnInit(){
        this.subscription = this.route.params.subscribe(params => {
            this.id = params['id'];
            this.initializeDataGathering();
            this.registerEvents();
        });

    }

    private registerEvents() {
        this.registerChangeInTasks();
        this.registerChangeInJGroups();
        this.registerChangeInTaskHistories();
        this.registerChangeInJUsers();
        this.registerChangeInJUserImg();
    }

    private initializeDataGathering() {
        this.principal.identity().then((user) => {
            this.userAccount = this.copyAccount(user);
            this.jUserService.queryJUser(user.id).subscribe(jUser => {this.activeJUser = jUser});
            this.jUserService.find(this.id).subscribe(jUser => {
                this.jUser = jUser;
                this.queryJImgOfJUser(jUser.id);
                this.queryJGroupsData(jUser.id);
                this.queryTasksHistories();
                this.queryTasksAssignedToJUser(jUser.id);
            });
        });
    }

    private queryJUserData(){
        this.userAccount = [];
        this.userService.findById(this.jUser.user.id).subscribe(user => {
            this.userAccount = user;
            this.jUserService.queryJUser(user.id).subscribe(jUser => {
                this.jUser = jUser;
                this.activeJUser = jUser;
            });
        });
        this.queryTasksHistories();
    }

    queryJImgOfJUser(id){
        this.jUserImgService.queryJImgOfJUser(id).subscribe(jUserImg => {
            this.jUserImg = jUserImg;
        });
    }

    queryJGroupsData(id) {
        this.jGroupService.querySpecificUserGroups(id).subscribe((res: Response) => {
                this.jGroups = res.json();

            for(let jGroup of this.jGroups) {
                this.queryTasks(jGroup.id);
                this.queryJGroupImgs(jGroup.id);
            }
        },
        (res: Response) => this.onError(res.json())
        );
    }

    queryTasks(id) {
        this.taskService.queryJGroupTasks(id).subscribe((res: Response) => {
                this.tasksMap[id] = res.json();
            },
            (res: Response) => this.onError(res.json())
        );
    }

    queryTasksHistories(){
        this.taskHistoryService.queryHistoriesOfTasksModifiedByUser(this.jUser.id).subscribe((res: Response) => {
            this.tasksHistories = res.json();
        },
            (res: Response) => this.onError(res.json())
        );
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

    checkPermissions() {
        if(this.activeJUser){
            let hasPermission:boolean = false;
            if(this.activeJUser.id == this.jUser.id){
                    hasPermission = true;
            }
            return hasPermission;
        }
        return false;
    }

    private initJGroupsReload(){
        this.principal.identity().then((user) => {
            this.jUserService.queryJUser(user.id).subscribe(jUser => {
                this.queryJGroupsData(jUser.id);
            });
        });
    }

    private initJGroupsTasksReload(){
        for(let jGroup of this.jGroups){
            this.queryTasks(jGroup.id);
        }
    }

    private initTasksHistoriesReload(){
        this.queryTasksHistories();
    }

    registerChangeInTasks(){
        this.eventSubscriber = this.eventManager.subscribe('taskListModification', () => {
            this.initJGroupsTasksReload();
            this.queryTasksAssignedToJUser(this.jUser.id);
        });
    }

    registerChangeInJGroups(){
        this.eventSubscriber = this.eventManager.subscribe('jGroupListModification', () => {
            this.initJGroupsReload();
        });
    }

    registerChangeInTaskHistories(){
        this.eventSubscriber = this.eventManager.subscribe('taskHistoryModification', () => {
            this.initTasksHistoriesReload();
        });
    }

    registerChangeInJUsers() {
        this.eventSubscriber = this.eventManager.subscribe('jUserListModification', (response) => this.queryJUserData());
    }

    registerChangeInJUserImg(){
        this.eventSubscriber = this.eventManager.subscribe('jUserImgListModification', (response) => {
            this.queryJImgOfJUser(this.jUser.id);
        })
    }

    saveMethod(obj) {
        if (obj.userFirstName){
            this.userAccount.firstName = obj.userFirstName;
            this.accountService.save(this.userAccount)
                .subscribe(
                    () => {
                        this.createSuccessAlert('Your changes have been saved successfully!')
                        this.eventManager.broadcast({name: 'jUserListModification', content: 'UPDATED'})
                    },
                    () => {
                        this.createErrorAlert('Your changes have not been saved due to some error.')
                    });
        }
        else if (obj.userLastName){
            this.userAccount.lastName = obj.userLastName;
            this.accountService.save(this.userAccount)
                .subscribe(
                    () => {
                        this.createSuccessAlert('Your changes have been saved successfully!')
                        this.eventManager.broadcast({name: 'jUserListModification', content: 'UPDATED'})
                    },
                    () => {
                        this.createErrorAlert('Your changes have not been saved due to some error.')
                    });
        }
        else if (obj.userEmailName){
            this.userAccount.email = obj.userEmailName;
            this.accountService.save(this.userAccount)
                .subscribe(
                    () => {
                        this.createSuccessAlert('Your changes have been saved successfully!')
                        this.eventManager.broadcast({name: 'jUserListModification', content: 'UPDATED'})
                    },
                    () => {
                        this.createErrorAlert('Your changes have not been saved due to some error.')
                    });
        }
    }

    copyAccount (account) {
        return {
            activated: account.activated,
            email: account.email,
            firstName: account.firstName,
            langKey: account.langKey,
            lastName: account.lastName,
            login: account.login,
            imageUrl: account.imageUrl
        };
    }

    createSuccessAlert(message: string) {
       AlertUtil.createSuccessAlert(message,this.msgs);
    }

    createErrorAlert(message: string) {
        AlertUtil.createErrorAlert(message, this.msgs);
    }

    // paginate(event) {
    //     event.first = this.tasksHistories[0];
    //     event.rows = 10;
    //     event.page = 11;
    //     event.pageCount = this.tasksHistories.length/10;
    // }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    ngOnDestroy(){
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }
}
