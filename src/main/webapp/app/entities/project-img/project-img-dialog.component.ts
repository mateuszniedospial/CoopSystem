import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, DataUtils } from 'ng-jhipster';

import { ProjectImg } from './project-img.model';
import { ProjectImgPopupService } from './project-img-popup.service';
import { ProjectImgService } from './project-img.service';
import { Project, ProjectService } from '../project';
@Component({
    selector: 'jhi-project-img-dialog',
    templateUrl: './project-img-dialog.component.html'
})
export class ProjectImgDialogComponent implements OnInit {

    projectImg: ProjectImg;
    authorities: any[];
    isSaving: boolean;

    projects: Project[];
    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: DataUtils,
        private alertService: AlertService,
        private projectImgService: ProjectImgService,
        private projectService: ProjectService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.projectService.query({filter: 'projectimg-is-null'}).subscribe((res: Response) => {
            if (!this.projectImg.project || !this.projectImg.project.id) {
                this.projects = res.json();
            } else {
                this.projectService.find(this.projectImg.project.id).subscribe((subRes: Project) => {
                    this.projects = [subRes].concat(res.json());
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

    setFileData($event, projectImg, field, isImage) {
        if ($event.target.files && $event.target.files[0]) {
            let $file = $event.target.files[0];
            if (isImage && !/^image\//.test($file.type)) {
                return;
            }
            this.dataUtils.toBase64($file, (base64Data) => {
                projectImg[field] = base64Data;
                projectImg[`${field}ContentType`] = $file.type;
            });
        }
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.projectImg.id !== undefined) {
            this.projectImgService.update(this.projectImg)
                .subscribe((res: ProjectImg) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.projectImgService.create(this.projectImg)
                .subscribe((res: ProjectImg) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: ProjectImg) {
        this.eventManager.broadcast({ name: 'projectImgListModification', content: 'OK'});
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

    trackProjectById(index: number, item: Project) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-project-img-popup',
    template: ''
})
export class ProjectImgPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private projectImgPopupService: ProjectImgPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.projectImgPopupService
                    .open(ProjectImgDialogComponent, params['id']);
            } else if(params['projectid']){
                this.modalRef = this.projectImgPopupService
                    .openCreate(ProjectImgDialogComponent, params);
            } else if(params['imgid']){
                this.modalRef = this.projectImgPopupService
                    .openCreate(ProjectImgDialogComponent, params);
            } else {
                this.modalRef = this.projectImgPopupService
                    .open(ProjectImgDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
