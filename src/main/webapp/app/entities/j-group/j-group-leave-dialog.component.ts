import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import {EventManager, AlertService} from 'ng-jhipster';

import { JGroup } from './j-group.model';
import { JGroupPopupService } from './j-group-popup.service';
import { JGroupService } from './j-group.service';
import {Response} from "@angular/http";

@Component({
    selector: 'jhi-j-group-leave-dialog',
    templateUrl: './j-group-leave-dialog.component.html'
})
export class JGroupLeaveDialogComponent {

    jGroup: JGroup;

    constructor(
        private jGroupService: JGroupService,
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private eventManager: EventManager
    ) {
    }

    clear () {
        this.activeModal.dismiss('cancel');
    }

    confirmLeave() {
        this.jGroupService.update(this.jGroup)
            .subscribe(
                (res: JGroup) => this.onSaveSuccess(res),
                (res: Response) => this.onSaveError(res.json())
            );

        // this.jGroupService.delete(id).subscribe(response => {
        //     this.eventManager.broadcast({
        //         name: 'jGroupListModification',
        //         content: 'Deleted an jGroup'
        //     });
        //     this.activeModal.dismiss(true);
        // });
    }

    private onSaveSuccess (result: JGroup) {
        this.eventManager.broadcast({ name: 'jGroupListModificationByLeave', content: 'OK'});
        this.activeModal.dismiss(result);
    }

    private onSaveError (error) {
        this.onError(error);
    }

    private onError (error) {
        this.alertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-j-group-delete-popup',
    template: ''
})
export class JGroupLeavePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private jGroupPopupService: JGroupPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.modalRef = this.jGroupPopupService
                .leave(JGroupLeaveDialogComponent, params);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
/**
 * Created by Chrono on 14.06.2017.
 */
