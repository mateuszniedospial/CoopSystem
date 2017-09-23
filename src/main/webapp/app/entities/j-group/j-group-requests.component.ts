import {Component, OnInit, OnDestroy} from "@angular/core";
import {EventManager} from "ng-jhipster";
import {JGroupService} from "./j-group.service";
import {JUserService} from "../j-user/j-user.service";
import {ActivatedRoute} from "@angular/router";
import {Principal} from "../../shared/auth/principal.service";
import {JoinJGroupRequest} from "../join-j-group-request/join-j-group-request.model";
import {JGroup} from "./j-group.model";
import {JUser} from "../j-user/j-user.model";
import {Subscription} from "rxjs";
import {JoinJGroupRequestService} from "../join-j-group-request/join-j-group-request.service";
import {JUserImg} from "../j-user-img/j-user-img.model";
import {JUserImgService} from "../j-user-img/j-user-img.service";
import {Message} from "primeng/components/common/api";
/**
 * Created by Chrono on 12.06.2017.
 */

@Component({
    selector: 'group-requests',
    templateUrl: './j-group-requests.component.html',
    styleUrls: [
        'j-group-profile.css'
    ]
})
export class JGroupRequestsComponent implements OnInit, OnDestroy {

    jUser: JUser;
    jGroup: JGroup;
    requests: JoinJGroupRequest[];
    requestsImgMap:{[key:string]:JUserImg} = {};

    doesBelong:boolean = false;

    subscription: any;
    eventSubscriber: Subscription;

    msgs: Message[] = [];

    constructor(
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private jUserService: JUserService,
        private jUserImgService: JUserImgService,
        private jGroupService: JGroupService,
        private joinJGroupRequestService: JoinJGroupRequestService,
        private eventManager: EventManager,
    ) {
    }

    ngOnInit(): void {
        this.principal.identity().then((user) => {
            this.jUserService.queryJUser(user.id).subscribe(user => {
                this.jUser = user;
                this.subscription = this.activatedRoute.params.subscribe(params => {
                    this.initializeData(params['id']);
                });
            });
        });

        this.registerEvents();
    }

    registerEvents(){
        this.registerChangeInRequests();
    }

    initializeData(id){
        this.jGroupService.find(id).subscribe(jGroup => {
            this.jGroup = jGroup;
            this.checkIfBelongsToGroup(jGroup);
            this.queryRequests(jGroup.id);
        });
    }

    private queryRequests(id){
        this.joinJGroupRequestService.querySpecificGroupRequests(id).subscribe( res => {
            this.requests = res.json();
            for(let request of res.json()){
                this.queryUserImgOfRequest(request);
            }
        })
    }

    reloadRequests(){
        this.joinJGroupRequestService.querySpecificGroupRequests(this.jGroup.id).subscribe( res => {
            this.requests = res.json();
            for(let request of res.json()){
                this.queryUserImgOfRequest(request);
            }
        })
    }

    queryUserImgOfRequest(request:JoinJGroupRequest){
        let idOfUser:number = request.whoRequested.id;
        this.jUserImgService.queryJImgOfJUser(idOfUser).subscribe(img => {
            this.requestsImgMap[request.id] = img;
        });
    }

    checkIfBelongsToGroup(jGroup: JGroup){
        for(let juser of jGroup.jusers){
            if(juser.id == this.jUser.id){
                this.doesBelong = true;
            }
        }
    }

    acceptRequest(request:JoinJGroupRequest){
        let whoRequested:JUser = request.whoRequested;
        let group:JGroup = this.jGroup;
        let usersOfGroup:JUser[] = group.jusers;
        usersOfGroup.push(whoRequested);
        group.jusers = usersOfGroup;
        this.jGroupService.update(group).subscribe(
            () => {
                this.rejectRequest(request);
                this.eventManager.broadcast({name: 'jGroupListModification', content: 'UPDATED'})
            },
            () => {
                this.createErrorAlert('Operations have not been made due to some error.')
            }
        );
    }

    rejectRequest(request:JoinJGroupRequest){
        this.joinJGroupRequestService.delete(request.id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'joinJGroupRequestListModification',
                content: 'Request accepted.'
            });
        });
    }

    createErrorAlert(message: string) {
        this.msgs.push({severity:'error', summary:'Error', detail:message});
        setTimeout(()=>{
            this.msgs=[];
        },2000);
    }

    registerChangeInRequests(){
        this.eventSubscriber = this.eventManager.subscribe('joinJGroupRequestListModification', (response) => {
            this.reloadRequests();
        });
    }

    ngOnDestroy(){
        this.eventManager.destroy(this.eventSubscriber);
    }
}
