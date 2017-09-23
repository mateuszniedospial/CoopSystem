///<reference path="../task-description/task-description.model.ts"/>
import {
    Component, OnInit, OnDestroy, ViewChild, ElementRef, AfterViewInit, AfterContentInit,
    AfterViewChecked
} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {Task, TaskLifeCycle, TaskPriority, TaskType} from './task.model';
import { TaskService } from './task.service';
import {TaskDescription} from '../task-description/task-description.model';
import {JUser} from '../j-user/j-user.model';
import {Comment} from '../comment/comment.model';
import {CommentService} from '../comment/comment.service';
import {Response} from '@angular/http';
import {Subscription} from 'rxjs';
import {EventManager, DataUtils} from 'ng-jhipster';
import {Principal} from '../../shared/auth/principal.service';
import {JUserService} from '../j-user/j-user.service';
import {JUserImgService} from '../j-user-img/j-user-img.service';
import {JUserImg} from '../j-user-img/j-user-img.model';
import {TaskDescriptionService} from '../task-description/task-description.service';
import {Message} from 'primeng/components/common/api';
import {Attachment} from '../attachment/attachment.model';
import {AttachmentService} from '../attachment/attachment.service';
import {JGroupService} from '../j-group/j-group.service';
import {JGroup} from '../j-group/j-group.model';
import {WorkLog} from '../work-log/work-log.model';
import {WorkLogService} from '../work-log/work-log.service';
import {TaskHistory} from '../task-history/task-history.model';
import {TaskHistoryService} from '../task-history/task-history.service';
import {AlertUtil} from "../../shared/util/alert-util";
import {DateUtil} from "../../shared/util/date-util";
import {TaskIconUtil} from "../../shared/util/task-icon-util";
import {JcommitService} from "../jcommit/jcommit.service";
import {Jcommit} from "../jcommit/jcommit.model";
import {User} from "../../shared/user/user.model";

@Component({
    selector: 'task-official-detail',
    templateUrl: './task-official-detail.component.html',
    styleUrls: [
        'task-official.css'
    ]
})
/**
 * To properly work this component task must be in group.
 */
export class TaskOfficialDetailComponent implements OnInit, OnDestroy {
    /**Static reference using in view .html */
    public dateUtilStaticRef : DateUtil = DateUtil;
    public taskStaticRef : Task = Task;
    public taskIconStaticRef : TaskIconUtil = TaskIconUtil;

    /**end of static*/

    activJUser: JUser;
    eventSubscriber: Subscription;
    task: Task;
    originTask: Task;
    jGroupWithUsers: JGroup;
    accountTask:any
    taskStatusItem: any;
    taskTypeItem :any;
    taskPriorityItem :any;
    commentsList : Comment[];
    assigneSuggestions: string[] = [] ;
    watchersSuggestions: string[];
    watchersUserNames : string[];
    description : TaskDescription;
    originDescription : TaskDescription;
    subscription: any;
    avatarList: JUserImg[]=[];
    attachmentsWihoutContent : Attachment[];
    msgs: Message[] = [];
    hasAttachments : boolean = false;
    subtasks : Task[] = [];
    hasSubtask : boolean = false;
    workLogs : WorkLog[] = [];
    sumOfWorks: number = 0;
    taskHistories: TaskHistory[];
    hasParent: boolean = false;
    commits: Jcommit[] = [];
    lastCommitDate: Date;

    constructor(
        private taskService: TaskService,
        private route: ActivatedRoute,
        private elRef:ElementRef,
        private commentService: CommentService,
        private eventManager: EventManager,
        private principal: Principal,
        private jUserService: JUserService,
        private jImgService: JUserImgService,
        private taskDescriptionService: TaskDescriptionService,
        private dataUtils: DataUtils,
        private attachmentService: AttachmentService,
        private jGroupService: JGroupService,
        private workLogService: WorkLogService,
        private taskHistoryService: TaskHistoryService,
        private jCommitService : JcommitService
    ) {

    }

    ngOnInit() {
        this.createEarlyDate();
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
        this.registerEvents();
    }

    private registerEvents() {
        this.registerChangeInComments();
        this.registerChangeInAttachment();
        this.registerChangeInWorkLog();
        this.registerChangeInSubtasks();
        this.registerChangeInTaskHistory();
    }

    private createEarlyDate() {
        this.description = new TaskDescription(12, null, null, 'Click me to add description...', this.task);
        this.commentsList = [];
        this.initDropDownItem();
    }

    private initDropDownItem() {
        this.taskStatusItem = Task.taskStatusItem;
        this.taskPriorityItem = Task.taskPriorityItem;
        this.taskTypeItem = Task.taskTypeItem;
    }

    load (id) {
        this.taskService.find(id).subscribe(task => {
            this.task = task;
            this.originTask = Object.assign({},task);
            this.loadCommentsList();
            this.principal.identity().then((account) => {
                this.accountTask = TaskOfficialDetailComponent.copyAccount(account);
                this.initJUserIDByJhiId(account.id);
            });
            this.loadTaskDescription();
            this.loadAttachmentLink();
            this.initGroupAccount();
            this.initSubtask();
            this.initWorkLog();
            this.initTaskHistories();
            this.initHasParentField();
            this.initCommits();
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);

    }

    changeStatus(input) {
        this.runIfHasPermission(() => {
            this.task.lifeCycle = input;
            this.taskService.update(this.task)
                .subscribe(
                    () => {
                        this.createSuccessAlert('Save task details succesfully');
                        // this.createTaskHistory('changeTaskBase', null);
                    },
                    () => {
                        this.createErrorAlert('Save task details failed')
                    });
        })
    }


    saveComment(obj,comment:Comment){
        let newComment : Comment = new Comment();
        newComment.author = comment.author;
        newComment.content = obj.comment;
        newComment.createdDate = comment.createdDate;
        newComment.modifyDate = new Date();
        newComment.id = comment.id;
        newComment.task = new Task();
        newComment.task.id = this.task.id;
        this.commentService.update(newComment).subscribe(
            () => {
                this.createSuccessAlert('Save comment  succesfully');
                },
            () => {this.createErrorAlert('Save comment failed')});
    }


    saveMethod(obj) {
        if(obj.description){
            this.description.task = new Task();
            this.description.content = obj.description;
            this.description.task.id= this.task.id;
            this.description.task.juser = this.activJUser;
            this.taskDescriptionService.update(this.description)
                .subscribe(
                    () => {
                        this.createSuccessAlert('Save task description succesfully')
                        },
                    () => {this.createErrorAlert('Save task description failed')});
        }  else {
            if(obj.status) this.task.lifeCycle = Task.translateStringToLifeCycle(obj.status);
            else if(obj.priority) this.task.priority = Task.translateStringToPriority(obj.priority);
            else if(obj.taskType) this.task.type = Task.translateStringToType(obj.taskType);
            else if(obj.estimateTime) this.task.estimateTime = obj.estimateTime;
            else if(obj.remainingTime) this.task.remainingTime = obj.remainingTime;
            else if(obj.remainingTime) this.task.remainingTime = obj.remainingTime;
            else if(obj.enviroment) this.task.enviroment = obj.enviroment;
            else if(obj.version) this.task.version = obj.version;
            else if(obj.taskTitle) this.task.title = obj.taskTitle;
            else if(obj.assigne) this.task.assignee =this.findJUserByParameter(obj.assigne, 'login');
            else if(obj.watchers) this.task.watchers = this.createWatchersArrrayByParameter(obj.watchers, 'login');
            this.taskService.update(this.task)
                .subscribe(
                () => {
                    this.createSuccessAlert('Save task details succesfully');
                    // this.createTaskHistory('changeTaskBase', null);
                },
                () => {this.createErrorAlert('Save task details failed')});;
        }
    }

    createSuccessAlert(message: string) {
        AlertUtil.createSuccessAlert(message, this.msgs);
    }

    createErrorAlert(message: string) {
        AlertUtil.createErrorAlert(message, this.msgs);
    }

    checkPermissions() {
        if(this.jGroupWithUsers && this.activJUser){
            let hasPermission:boolean = false;
            let jusers:JUser[] = this.jGroupWithUsers['jusers'];
            jusers.forEach((it)=>{
                if(it.id == this.activJUser.id){
                    hasPermission = true;
                }
            })
            return hasPermission;
        }
        return false;
    }

    isStatus(status:any){
        if(status == this.task.lifeCycle) return true
        return false;
    }

    searchAssigne(event)  {
        let result = this.findAssigneByQuery(event.query);
        this.assigneSuggestions = result;
    }

    private findAssigneByQuery(query:string) :string[]{
        let possibleLogins: string[]=[];
        this.jGroupWithUsers.jusers.forEach((it:JUser) =>
            possibleLogins.push(it.user.login)
        );
        let result =  possibleLogins.filter((it:string)=>{
            return it.includes(query, 0)
        });
        return result;
    }

    searchWatchers(event) {
        this.watchersSuggestions = this.findAssigneByQuery(event.query)
    }

    clickAssingne() {
        this.runIfHasPermission(() => {
            this.task.assignee = this.activJUser;
            this.taskService.update(this.task).subscribe(
                () => {
                    this.createSuccessAlert('Save assigne succesfully')
                },
                () => {
                    this.createErrorAlert('Save assigne failed')
                });
        })
    }

    clickAddSubtask(){
    }

    toJSON(inp){
      return  JSON.stringify(inp)
    }

    private onError(input: any) {
        console.log('Problem:' + input );
    }

    private loadCommentsList() {
        this.commentService.query({
            'taskid': this.task.id
        }).subscribe((res: Response) => {
                var response: any[];
                response = res.json();
                this.commentsList = [];
                var i =0;
                response.forEach((it:Comment)=>{
                    this.commentsList[i++] = it;
                    if(it.author){
                        if(it.author.user)
                            this.initAvatar(it.author.id);
                    }
                })
            },
            (res: Response) => this.onError(res.json())
        );
    }

    registerChangeInComments() {
        this.eventSubscriber = this.eventManager.subscribe('commentListModification', (response) => this.loadCommentsList());
    }
    registerChangeInWorkLog() {
        this.eventSubscriber = this.eventManager.subscribe('workLogListModification', (response) => this.initWorkLog());
    }

    registerChangeInSubtasks(){
        this.eventSubscriber = this.eventManager.subscribe('taskListModification', (respone) => {this.initSubtask()});
    }

    registerChangeInTaskHistory() {
        this.eventSubscriber = this.eventManager.subscribe('taskHistoryModification', (response) =>{this.initTaskHistories()});
    }
    static copyAccount (account) {
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

    initJUserIDByJhiId(jhiId : number ){
        this.jUserService.query({
            'jhiId' : jhiId
        }).subscribe(
            (res: Response) => {
                this.activJUser =res.json()
                this.task.juser= this.activJUser;
                ;},
            (res: Response) => this.onError(res.json()))
    }

    getAvatarById(jId:number): JUserImg{
        if(this.avatarList.length>0){
            return this.avatarList.find((it:JUserImg)=>it.juser.id ==jId)
        } else {
            return null
        }
    }

    private avatarRequestSendedMap: { [key:string]:boolean;}={}
    initAvatar(jId:number) {
        if(!this.avatarRequestSendedMap[jId]){
            this.avatarRequestSendedMap[jId] = true;
            this.jImgService.query({
                'juserId': jId
            }).subscribe((res: Response) => {
                    var response: any[];
                    response = res.json();
                    this.avatarList[this.avatarList.length] = response;
                },
                (res: Response) => this.onError(res.json()))
        }
    }

    private loadTaskDescription() {
        this.taskDescriptionService.queryByTaskId({
            'query' : this.task.id
        }).subscribe((res: Response) => {
                let resDescription: TaskDescription = res.json()
                if(resDescription.content == ""){
                    this.description.content = "Click me to add description...";
                    this.description.id = resDescription.id;
                } else {
                    this.description = resDescription
                }
                this.originDescription =  Object.assign({},  resDescription);;
        },
            (res: Response) => this.onError(res.json()))
    }


    uploadAttachment(){
        this.runIfHasPermission(() => {
            let uploadEl = this.elRef.nativeElement.querySelector('#upfile');
            uploadEl.click();
        })
    }

    attachment : Attachment = new Attachment();
    sendFile($event){
        let uploadEl = this.elRef.nativeElement.querySelector('#upfile');
        let name = uploadEl.value;
        if(name){
            this.attachment.author = this.activJUser;
            this.attachment.task = new Task();
            this.attachment.task.id = this.task.id;
            this.attachment.name = name;
            this.setFileData($event);
        }
    }

    setFileData($event) {
        if ($event.target.files && $event.target.files[0]) {
            let $file = $event.target.files[0];
            this.attachment.name = $file.name;
            let field = 'content';
            this.dataUtils.toBase64($file, (base64Data) => {
                this.attachment[field] = base64Data;
                this.attachment[`${field}ContentType`] = $file.type;
                this.eventManager.broadcast({name: 'attachmentListModification', content: 'OK'});

            });
        } else {
            console.log('File dont load');
        }
    }

    registerChangeInAttachment() {
        this.eventSubscriber = this.eventManager.subscribe('attachmentListModification', (response) => {
                if (this.attachment.content) {
                    this.attachmentService.create(this.attachment)
                        .subscribe(
                            () => {
                                this.createSuccessAlert('Upload attachment succesfully')
                                this.loadAttachmentLink()},
                            () => {
                                this.createErrorAlert('Upload attachment failed')});
                } else {
                    this.createErrorAlert('Upload attachment failed. Problem with load date in client side. Try again');
                }
            }
        );
    }

    private loadAttachmentLink() {
        this.attachmentService.queryBy({
            'query': this.task.id
        }, 'byTaskId')
            .subscribe(
            (res: Response) => {
                var response: any[];
                response = res.json();
                this.attachmentsWihoutContent = response;
                this.hasAttachments = (this.attachmentsWihoutContent.length > 0)
            },
            (res: Response) => this.onError(res.json()))
    }


    deleteFile(attachment:Attachment) {
        this.runIfHasPermission(() => {
            this.attachmentService.delete(attachment.id).subscribe(
                () => {
                    this.createSuccessAlert('Delete attachment succesfully');
                    this.loadAttachmentLink()
                },
                () => {
                    this.createErrorAlert('Delete attachment failed')
                });
        })
    }

    checkCommentPermissions(comment: Comment){
       if(this.activJUser){
           return comment.author.id == this.activJUser.id;
       } else {
           return false;
       }
    }

    private initGroupAccount() {
        this.task.jGroup = this.task.jgroup;//backend response jgroup instead jGroup
        this.jGroupService.find(this.task.jGroup.id)
            .subscribe((res: Response) => {
                    this.jGroupWithUsers = res;
                this.initWatchers()
                },
                () => {this.createErrorAlert('init group failed')}
            );
    }

    private findJUserByParameter(input: any, parameter: any) : JUser {
        let result: JUser;
        this.jGroupWithUsers.jusers.forEach((jUser: JUser)=>{
            if(jUser.user[parameter] == input) {
                result = jUser;
            }
        })
        return result;
    }

    private initWatchers() {
        this.watchersUserNames = [];
        this.task.watchers.forEach((jUser:JUser)=>{
            this.watchersUserNames.push(jUser.user.login);
        })
    }

    private createWatchersArrrayByParameter(input: string[] , field: string) {
        let result: JUser[] = [];
        input.forEach(inputEntry=>{
            result.push(this.findJUserByParameter(inputEntry, field));
        })
        return result;
    }

    private initSubtask() {
        let taskId = this.task.id;
        this.taskService.queryByParenId({'query': taskId}).subscribe(
            (res: Response) => {
                let response: any[] = res.json();
                this.subtasks = response;
                this.hasSubtask = response.length > 0 ;
            },
            (res: Response) => this.onError(res.json())
        )
    }

    private initWorkLog() {
        this.sumOfWorks = 0;
        this.workLogService.queryByTaskId({'query': this.task.id}).subscribe(
            (res: Response) => {
                let response: any[] = res.json();
                this.workLogs = response;
                this.workLogs.forEach(it=>{
                    this.sumOfWorks = this.sumOfWorks+ it.timeInHour;
                })
            },
            (res: Response) => this.onError(res.json())
        )
    }


    private initTaskHistories() {
        this.taskHistoryService.queryByTaskId({
            'query':this.task.id
        }).subscribe(
            (res: Response) => {
                let response: any[] = res.json();
                this.taskHistories = response;
            },
            (res: Response) => this.onError(res.json())
        )
    }

    runIfHasPermission(method:any){
        if(this.checkPermissions()) {
            method()
        } else {
            this.createErrorAlert('You have not permission to modify task.')
        }
    }

    private initHasParentField() {
        this.hasParent = false;
        if(this.task.parent) {
            this.hasParent = true;
        }
    }

    private initCommits() {
        this.jCommitService.queryByTaskId(this.task.id).subscribe((res: Response) => {
            let response: any[] = res.json();
            this.commits = response;
            if(this.commits.length > 0)
                this.lastCommitDate = this.commits[0].commitDate;
        },
            (res: Response) => this.onError(res.json()));
    }

}
