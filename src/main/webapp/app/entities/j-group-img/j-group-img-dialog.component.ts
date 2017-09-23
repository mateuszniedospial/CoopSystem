import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, DataUtils } from 'ng-jhipster';

import { JGroupImg } from './j-group-img.model';
import { JGroupImgPopupService } from './j-group-img-popup.service';
import { JGroupImgService } from './j-group-img.service';
import { JGroup, JGroupService } from '../j-group';
@Component({
    selector: 'jhi-j-group-img-dialog',
    templateUrl: './j-group-img-dialog.component.html'
})
export class JGroupImgDialogComponent implements OnInit {

    jGroupImg: JGroupImg;
    authorities: any[];
    isSaving: boolean;

    jgroups: JGroup[];
    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: DataUtils,
        private alertService: AlertService,
        private jGroupImgService: JGroupImgService,
        private jGroupService: JGroupService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.jGroupService.query({filter: 'jgroupimg-is-null'}).subscribe((res: Response) => {
            if (!this.jGroupImg.jgroup || !this.jGroupImg.jgroup.id) {
                this.jgroups = res.json();
            } else {
                this.jGroupService.find(this.jGroupImg.jgroup.id).subscribe((subRes: JGroup) => {
                    this.jgroups = [subRes].concat(res.json());
                }, (subRes: Response) => this.onError(subRes.json()));
            }
        }, (res: Response) => this.onError(res.json()));
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData($event, jGroupImg, field, isImage) {
        if ($event.target.files && $event.target.files[0]) {
            let $file = $event.target.files[0];
            if (isImage && !/^image\//.test($file.type)) {
                return;
            }
            this.dataUtils.toBase64($file, (base64Data) => {
                jGroupImg[field] = base64Data;
                jGroupImg[`${field}ContentType`] = $file.type;
            });
        }
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.jGroupImg.id !== undefined) {
            this.jGroupImgService.update(this.jGroupImg)
                .subscribe((res: JGroupImg) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.jGroupImgService.create(this.jGroupImg)
                .subscribe((res: JGroupImg) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: JGroupImg) {
        this.eventManager.broadcast({ name: 'jGroupImgListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-j-group-img-popup',
    template: ''
})
export class JGroupImgPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private jGroupImgPopupService: JGroupImgPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.jGroupImgPopupService
                    .open(JGroupImgDialogComponent, params['id']);
            } else if (params['jgroupid']) {
                this.modalRef = this.jGroupImgPopupService
                    .openCreate(JGroupImgDialogComponent, params);
            } else {
                this.modalRef = this.jGroupImgPopupService
                    .open(JGroupImgDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
