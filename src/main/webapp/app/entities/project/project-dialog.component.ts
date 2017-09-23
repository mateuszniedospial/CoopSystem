import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, DataUtils } from 'ng-jhipster';

import { Project } from './project.model';
import { ProjectPopupService } from './project-popup.service';
import { ProjectService } from './project.service';
import { JGroup, JGroupService } from '../j-group';
import {Principal} from "../../shared/auth/principal.service";
import {JUserService} from "../j-user/j-user.service";
@Component({
    selector: 'jhi-project-dialog',
    templateUrl: './project-dialog.component.html'
})
export class ProjectDialogComponent implements OnInit {

    project: Project;
    authorities: any[];
    isSaving: boolean;
    jgroups: JGroup[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: DataUtils,
        private alertService: AlertService,
        private projectService: ProjectService,
        private principal: Principal,
        private jUserService: JUserService,
        private jGroupService: JGroupService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        // this.jGroupService.query().subscribe(
        //     (res: Response) => { this.jgroups = res.json(); }, (res: Response) => this.onError(res.json()));
        this.principal.identity().then((user) => {
            this.jUserService.queryJUser(user.id).subscribe(jUser => {
                this.jGroupService.querySpecificUserGroups(jUser.id).subscribe(res => {
                    this.jgroups = res.json();
                })
            })
        });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData($event, project, field, isImage) {
        if ($event.target.files && $event.target.files[0]) {
            let $file = $event.target.files[0];
            if (isImage && !/^image\//.test($file.type)) {
                return;
            }
            this.dataUtils.toBase64($file, (base64Data) => {
                project[field] = base64Data;
                project[`${field}ContentType`] = $file.type;
            });
        }
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.project.id !== undefined) {
            this.projectService.update(this.project)
                .subscribe((res: Project) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.projectService.create(this.project)
                .subscribe((res: Project) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: Project) {
        this.eventManager.broadcast({ name: 'projectListModification', content: 'OK'});
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
    selector: 'jhi-project-popup',
    template: ''
})
export class ProjectPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private projectPopupService: ProjectPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.projectPopupService
                    .open(ProjectDialogComponent, params['id']);
            } else {
                this.modalRef = this.projectPopupService
                    .open(ProjectDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
