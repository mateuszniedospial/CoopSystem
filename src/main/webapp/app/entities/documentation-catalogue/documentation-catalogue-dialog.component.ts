import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { DocumentationCatalogue } from './documentation-catalogue.model';
import { DocumentationCataloguePopupService } from './documentation-catalogue-popup.service';
import { DocumentationCatalogueService } from './documentation-catalogue.service';
import { Project, ProjectService } from '../project';
@Component({
    selector: 'jhi-documentation-catalogue-dialog',
    templateUrl: './documentation-catalogue-dialog.component.html'
})
export class DocumentationCatalogueDialogComponent implements OnInit {

    documentationCatalogue: DocumentationCatalogue;
    authorities: any[];
    isSaving: boolean;

    parents: DocumentationCatalogue[];

    projects: Project[];
    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private documentationCatalogueService: DocumentationCatalogueService,
        private projectService: ProjectService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.documentationCatalogueService.query({filter: 'parent-is-null'}).subscribe((res: Response) => {
            if (!this.documentationCatalogue.parent || !this.documentationCatalogue.parent.id) {
                this.parents = res.json();
            } else {
                this.documentationCatalogueService.find(this.documentationCatalogue.parent.id).subscribe((subRes: DocumentationCatalogue) => {
                    this.parents = [subRes].concat(res.json());
                }, (subRes: Response) => this.onError(subRes.json()));
            }
        }, (res: Response) => this.onError(res.json()));
        this.projectService.query().subscribe(
            (res: Response) => { this.projects = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.documentationCatalogue.id !== undefined) {
            this.documentationCatalogueService.update(this.documentationCatalogue)
                .subscribe((res: DocumentationCatalogue) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.documentationCatalogueService.create(this.documentationCatalogue)
                .subscribe((res: DocumentationCatalogue) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: DocumentationCatalogue) {
        this.eventManager.broadcast({ name: 'documentationCatalogueListModification', content: 'OK'});
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

    trackDocumentationCatalogueById(index: number, item: DocumentationCatalogue) {
        return item.id;
    }

    trackProjectById(index: number, item: Project) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-documentation-catalogue-popup',
    template: ''
})
export class DocumentationCataloguePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private documentationCataloguePopupService: DocumentationCataloguePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.documentationCataloguePopupService
                    .open(DocumentationCatalogueDialogComponent, params['id']);
            } else if( params['projectid']){
                this.modalRef = this.documentationCataloguePopupService
                    .openCreate(DocumentationCatalogueDialogComponent, params);
            } else {
                this.modalRef = this.documentationCataloguePopupService
                    .open(DocumentationCatalogueDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
