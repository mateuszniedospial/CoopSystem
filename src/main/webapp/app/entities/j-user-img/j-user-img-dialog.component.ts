import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, DataUtils } from 'ng-jhipster';

import { JUserImg } from './j-user-img.model';
import { JUserImgPopupService } from './j-user-img-popup.service';
import { JUserImgService } from './j-user-img.service';
import { JUser, JUserService } from '../j-user';
@Component({
    selector: 'jhi-j-user-img-dialog',
    templateUrl: './j-user-img-dialog.component.html'
})
export class JUserImgDialogComponent implements OnInit {

    jUserImg: JUserImg;
    authorities: any[];
    isSaving: boolean;

    jusers: JUser[];
    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: DataUtils,
        private alertService: AlertService,
        private jUserImgService: JUserImgService,
        private jUserService: JUserService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.jUserService.query({filter: 'juserimg-is-null'}).subscribe((res: Response) => {
            if (!this.jUserImg.juser || !this.jUserImg.juser.id) {
                this.jusers = res.json();
            } else {
                this.jUserService.find(this.jUserImg.juser.id).subscribe((subRes: JUser) => {
                    this.jusers = [subRes].concat(res.json());
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

    setFileData($event, jUserImg, field, isImage) {
        if ($event.target.files && $event.target.files[0]) {
            let $file = $event.target.files[0];
            if (isImage && !/^image\//.test($file.type)) {
                return;
            }
            this.dataUtils.toBase64($file, (base64Data) => {
                jUserImg[field] = base64Data;
                jUserImg[`${field}ContentType`] = $file.type;
            });
        }
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.jUserImg.id !== undefined) {
            this.jUserImgService.update(this.jUserImg)
                .subscribe((res: JUserImg) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.jUserImgService.create(this.jUserImg)
                .subscribe((res: JUserImg) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: JUserImg) {
        this.eventManager.broadcast({ name: 'jUserImgListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-j-user-img-popup',
    template: ''
})
export class JUserImgPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private jUserImgPopupService: JUserImgPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.jUserImgPopupService
                    .open(JUserImgDialogComponent, params['id']);
            } else if(params['userid']) {
                this.modalRef = this.jUserImgPopupService
                    .openCreate(JUserImgDialogComponent, params);
            } else {
                this.modalRef = this.jUserImgPopupService
                    .open(JUserImgDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
