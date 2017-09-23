import {Component, OnInit, OnDestroy, ElementRef} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Poker } from './poker.model';
import { PokerPopupService } from './poker-popup.service';
import { PokerService } from './poker.service';
import { Sprint, SprintService } from '../sprint';
import { JGroup, JGroupService } from '../j-group';
// import { PokerVotes, PokerVotesService } from './poker-votes';
import { Task, TaskService } from '../task';
import {JUser} from "../j-user/j-user.model";
import {UserService} from "../../shared/user/user.service";
import {JUserService} from "../j-user/j-user.service";
import {Principal} from "../../shared/auth/principal.service";
import {Message} from "primeng/components/common/api";
import {PokerVotes} from "./poker-votes.model";
import {PokerVotesService} from "./poker-votes.service";
@Component({
    selector: 'jhi-poker-official-dialog',
    templateUrl: './poker-official-dialog.component.html',
    styleUrls: [
        'poker.css'
    ]
})
export class PokerOfficialDialogComponent implements OnInit {

    poker: Poker;
    authorities: any[];
    isSaving: boolean;

    sprints: Sprint[];

    jgroup: JGroup;
    myVote : PokerVotes;
    pokervotes: PokerVotes[];
    activeJUser: JUser;

    tasks: Task[];
    msgs: Message[] = [];
    isNewPokerRequired :boolean = false;
    isNotStoped: boolean = true;
    isStoped:boolean = false;
    average: number = 0;
    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private pokerService: PokerService,
        private sprintService: SprintService,
        private jGroupService: JGroupService,
        private pokerVotesService: PokerVotesService,
        private taskService: TaskService,
        private eventManager: EventManager,
        private jUserService: JUserService,
        private principal: Principal,
        private elRef:ElementRef
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.principal.identity().then((account) => {
            this.initJUserIDByJhiId(account.id);
        });
        this.initGroup();
        this.createNewPokerRequired()

    }

    private initGroup() {
        this.jGroupService.find(this.poker.jGroup.id).subscribe(
            (res: Response) => {
                this.jgroup = res;
            },
            () => {
                console.log("Problem")
            });
    }
    initJUserIDByJhiId(jhiId : number ){
        this.jUserService.query({
            'jhiId' : jhiId
        }).subscribe(
            (res: Response) => {this.activeJUser =res.json();},
            (res: Response) => this.onError(res.json()))
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    saveEstimate () {
        this.taskService.updateEstimate(this.poker.task).subscribe( () => {
                this.createSuccessAlert('Estimate save')},
            () => {
                this.createErrorAlert('Try again')});

    }

    private onSaveSuccess (result: Poker) {
        this.eventManager.broadcast({ name: 'pokerListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError (error) {
        this.isSaving = false;
        this.onError(error);
    }

    private onError (error) {
        this.alertService.error(error.message, null, null);
    }

    vote(vote:number) {
        this.pokerService.find(this.poker.id).subscribe((res:Response) => {
                this.poker = res;
                if(this.poker.isStoped){
                    this.createErrorAlert('The game is over.')
                }else {
                    this.myVote = new PokerVotes();
                    this.myVote.juser = this.activeJUser;
                    this.myVote.vote = vote;
                    this.myVote.poker = this.poker;
                    this.saveVote();
                }
            },
            () => {this.createErrorAlert('Try again')});

    }

    saveVote(){
        this.pokerVotesService.create(this.myVote).subscribe(
            () => {
                this.createSuccessAlert('Your choice save')},
            () => {
                this.createErrorAlert('Try again')});
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

    private createPoker() {
        this.pokerService.create(this.poker).subscribe(
            (res:Response) => {
                this.createSuccessAlert('Create new game')
                this.poker = res;
                this.initPokerVotes()
            },
            () => {this.createErrorAlert('Try again')});
    }


    private createNewPokerRequired() {
        this.pokerService.queryByTaskId(this.poker.task.id).subscribe(
            (res:Response) => {
                    this.poker = res.json()
                    this.initPokerVotes();
            },
            (res:Response) => {
                    this.createPoker()});
    }
    private initPokerVotes() {
        this.pokerVotesService.queryByPokerId(this.poker.id).subscribe(
            (res:Response)=>{
                let response : PokerVotes[] = res.json()
                this.pokervotes = response;
                this.calculateAverage();
            }, ()=>{console.log("no votes")})
    }
    stopGame() {
        this.isNotStoped = false;
        this.isStoped= true;
        this.poker.isStoped = true;
        this.pokerService.update(this.poker).subscribe(
            () => {
                this.initPokerVotes();
                this.createSuccessAlert('End the game');
            },
            (res:Response) => {
                this.createErrorAlert('Try again')});;
    }
    showCards(){
        this.initPokerIfIsPokerEnded()
    }

    findCard(juser:JUser){
            let pokerVote:PokerVotes = this.pokervotes.find((vote:PokerVotes)=>vote.juser.id == juser.id)
            if(pokerVote){
                return pokerVote.vote;
            }
            return '?';
    }

    private initPokerIfIsPokerEnded() {
        this.pokerService.find(this.poker.id).subscribe((res:Response) => {
                this.poker = res;
                if(this.poker.isStoped){
                    this.initPokerVotes()
                    this.isNotStoped = false;
                    this.isStoped = true;
                }else {
                    this.createErrorAlert('Game is not ended')
                }
            },
            () => {this.createErrorAlert('Try again')});
    }
    restartGame(){
        this.isNotStoped = true;
        this.isStoped = false;
        this.poker.isStoped = false;
        this.pokerService.update(this.poker).subscribe(
            () => {
                this.createSuccessAlert('Restart the game');
            },
            (res: Response) => {
                this.createErrorAlert('Try again')
            });
        this.pokerVotesService.deleteByPokerId(this.poker.id).subscribe(
            () => {
                this.createSuccessAlert('Delete all votes');
            },
            (res: Response) => {
                this.createErrorAlert('Try again')
            });
    }

    private calculateAverage() {
        let sum:number=0;
        let numberOfVotes=0;
        this.pokervotes.forEach((pv:PokerVotes)=>{
            sum+=pv.vote;
            numberOfVotes++;
        })
        this.average = sum/numberOfVotes;
    }
}

@Component({
    selector: 'jhi-poker-popup',
    template: ''
})
export class PokerOfficialPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private pokerPopupService: PokerPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.pokerPopupService
                    .open(PokerOfficialDialogComponent, params['id']);
            } else {
                this.modalRef = this.pokerPopupService
                    .openOfficial(PokerOfficialDialogComponent, params['taskId'], params['jGroupId']);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
