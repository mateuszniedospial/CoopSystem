import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { JoinJGroupRequest } from './join-j-group-request.model';
import { JoinJGroupRequestPopupService } from './join-j-group-request-popup.service';
import { JoinJGroupRequestService } from './join-j-group-request.service';
import { JGroup, JGroupService } from '../j-group';
import { JUser, JUserService } from '../j-user';
@Component({
    selector: 'jhi-join-j-group-request-dialog',
    templateUrl: './join-j-group-request-dialog.component.html'
})
export class JoinJGroupRequestDialogComponent implements OnInit {

    joinJGroupRequest: JoinJGroupRequest;
    authorities: any[];
    isSaving: boolean;

    jgroups: JGroup[];

    whorequesteds: JUser[];
    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private joinJGroupRequestService: JoinJGroupRequestService,
        private jGroupService: JGroupService,
        private jUserService: JUserService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.jGroupService.query().subscribe(
            (res: Response) => { this.jgroups = res.json(); }, (res: Response) => this.onError(res.json()));
        this.jUserService.query({filter: 'joinjgrouprequest-is-null'}).subscribe((res: Response) => {
            if (!this.joinJGroupRequest.whoRequested || !this.joinJGroupRequest.whoRequested.id) {
                this.whorequesteds = res.json();
            } else {
                this.jUserService.find(this.joinJGroupRequest.whoRequested.id).subscribe((subRes: JUser) => {
                    this.whorequesteds = [subRes].concat(res.json());
                }, (subRes: Response) => this.onError(subRes.json()));
            }
        }, (res: Response) => this.onError(res.json()));
    }

    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.joinJGroupRequest.id !== undefined) {
            this.joinJGroupRequestService.update(this.joinJGroupRequest)
                .subscribe((res: JoinJGroupRequest) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.joinJGroupRequestService.create(this.joinJGroupRequest)
                .subscribe((res: JoinJGroupRequest) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: JoinJGroupRequest) {
        this.eventManager.broadcast({ name: 'joinJGroupRequestListModification', content: 'OK'});
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

    trackJGroupById(index: number, item: JGroup) {
        return item.id;
    }

    trackJUserById(index: number, item: JUser) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-join-j-group-request-popup',
    template: ''
})
export class JoinJGroupRequestPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private joinJGroupRequestPopupService: JoinJGroupRequestPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.joinJGroupRequestPopupService
                    .open(JoinJGroupRequestDialogComponent, params['id']);
            } else if( params['jgroupid']){
                this.modalRef = this.joinJGroupRequestPopupService
                    .openCreate(JoinJGroupRequestDialogComponent, params);
            } else {
                this.modalRef = this.joinJGroupRequestPopupService
                    .open(JoinJGroupRequestDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
