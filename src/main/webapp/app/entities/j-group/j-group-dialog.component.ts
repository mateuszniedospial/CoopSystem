import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, DataUtils } from 'ng-jhipster';

import { JGroup } from './j-group.model';
import { JGroupPopupService } from './j-group-popup.service';
import { JGroupService } from './j-group.service';
import { JUser, JUserService } from '../j-user';
import {Project} from "../project/project.model";
import {ProjectService} from "../project/project.service";
@Component({
    selector: 'jhi-j-group-dialog',
    templateUrl: './j-group-dialog.component.html'
})
export class JGroupDialogComponent implements OnInit {

    jGroup: JGroup;
    project: Project;
    userToAdd: JUser;
    authorities: any[];
    isSaving: boolean;

    jusers: JUser[];
    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: DataUtils,
        private alertService: AlertService,
        private jGroupService: JGroupService,
        private projectService: ProjectService,
        private jUserService: JUserService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.jUserService.query().subscribe(
            (res: Response) => {
                this.jusers = res.json();
                let options:JUser[] = [];
                for(let user of this.jusers){
                    if(user.id != this.userToAdd.id){
                        options.push(user);
                    }
                }
                this.jusers = options;
            },
            (res: Response) => this.onError(res.json()));
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData($event, jGroup, field, isImage) {
        if ($event.target.files && $event.target.files[0]) {
            let $file = $event.target.files[0];
            if (isImage && !/^image\//.test($file.type)) {
                return;
            }
            this.dataUtils.toBase64($file, (base64Data) => {
                jGroup[field] = base64Data;
                jGroup[`${field}ContentType`] = $file.type;
            });
        }
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.jGroup.id !== undefined) {
            this.jGroupService.update(this.jGroup)
                .subscribe((res: JGroup) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.jGroup.jusers.push(this.userToAdd);
            this.jGroupService.create(this.jGroup).subscribe(
                (res: JGroup) => {
                    this.onSaveSuccess(res);
                },
                (res: Response) => this.onSaveError(res.json())
            );


        }
    }

    private onSaveSuccess (result: JGroup) {
        this.project.jgroups.push(result);
        this.projectService.update(this.project).subscribe(
            (res: Project) => this.onSaveSuccessProject(res),
            (res: Response) => this.onSaveErrorProject(res.json())
        );
        this.eventManager.broadcast({ name: 'projectGroupsListModification', content: 'OK'});
        this.eventManager.broadcast({ name: 'jGroupListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveSuccessProject(result: Project){
        this.eventManager.broadcast({name: 'projectListModification', content: 'OK'});
    }

    private onSaveError (error) {
        this.isSaving = false;
        this.onError(error);
    }

    private onSaveErrorProject(error){
        this.onError(error);
    }

    private onError (error) {
        this.alertService.error(error.message, null, null);
    }

    trackJUserById(index: number, item: JUser) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-j-group-popup',
    template: ''
})
export class JGroupPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private jGroupPopupService: JGroupPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.jGroupPopupService
                    .open(JGroupDialogComponent, params['id']);
            } else if(params['projectid']) {
                this.modalRef = this.jGroupPopupService
                    .openCreate(JGroupDialogComponent, params);
            } else {
                this.modalRef = this.jGroupPopupService
                    .open(JGroupDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
