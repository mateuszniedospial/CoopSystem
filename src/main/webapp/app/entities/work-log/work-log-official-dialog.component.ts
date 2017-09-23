import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { WorkLog } from './work-log.model';
import { WorkLogPopupService } from './work-log-popup.service';
import { WorkLogService } from './work-log.service';
import { JUser, JUserService } from '../j-user';
import { Task, TaskService } from '../task';
@Component({
    selector: 'work-official-log-dialog',
    templateUrl: './work-log-official-dialog.component.html'
})
export class WorkLogOfficialDialogComponent implements OnInit {

    workLog: WorkLog;
    authorities: any[];
    isSaving: boolean;

    jusers: JUser[];

    tasks: Task[];
    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private workLogService: WorkLogService,
        private jUserService: JUserService,
        private taskService: TaskService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.jUserService.query({filter: 'worklog-is-null'}).subscribe((res: Response) => {
            if (!this.workLog.juser || !this.workLog.juser.id) {
                this.jusers = res.json();
            } else {
                this.jUserService.find(this.workLog.juser.id).subscribe((subRes: JUser) => {
                    this.jusers = [subRes].concat(res.json());
                }, (subRes: Response) => this.onError(subRes.json()));
            }
        }, (res: Response) => this.onError(res.json()));
        this.taskService.query().subscribe(
            (res: Response) => { this.tasks = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.workLog.id !== undefined) {
            this.workLogService.update(this.workLog)
                .subscribe((res: WorkLog) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.workLogService.create(this.workLog)
                .subscribe((res: WorkLog) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: WorkLog) {
        this.eventManager.broadcast({ name: 'workLogListModification', content: 'OK'});
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

    trackJUserById(index: number, item: JUser) {
        return item.id;
    }

    trackTaskById(index: number, item: Task) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-work-log-popup',
    template: ''
})
export class WorkLogOfficialPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private workLogPopupService: WorkLogPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.modalRef = this.workLogPopupService.openOfficial(WorkLogOfficialDialogComponent, params);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
