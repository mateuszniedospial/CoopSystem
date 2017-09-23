import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { Project } from './project.model';
import { ProjectPopupService } from './project-popup.service';
import { ProjectService } from './project.service';
import {ProjectDocumentationComponent} from "./project-documentation.component";
import {ProjectDocumentation} from "../project-documentation/project-documentation.model";
import {DocumentationHistory} from "../documentation-history/documentation-history.model";
import {DocumentationHistoryService} from "../documentation-history/documentation-history.service";
import {ProjectDocumentationService} from "../project-documentation/project-documentation.service";

@Component({
    selector: 'jhi-project-documentation-revert-dialog',
    templateUrl: './project-documentation-revert-dialog.component.html',
    providers: [ProjectDocumentationComponent]
})
export class ProjectDocumentationRevertDialogComponent {

    project: Project;
    doc: ProjectDocumentation;
    history: DocumentationHistory;
    revertTo: any;

    constructor(
        private projectDocumentationComponent: ProjectDocumentationComponent,
        private projectDocumentationService: ProjectDocumentationService,
        private historyDocumentationService: DocumentationHistoryService,
        private projectService: ProjectService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear () {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete (id: number) {
        this.projectService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'projectListModification',
                content: 'Deleted an project'
            });
            this.activeModal.dismiss(true);
        });
    }

    confirmRevert(doc: number, revertTo: any){
        let copy:ProjectDocumentation = this.doc;

        this.historyDocumentationService.find(revertTo).subscribe(documentationHistory => {
            this.history = documentationHistory;
            copy.version = documentationHistory.version;
            copy.content = documentationHistory.content;

            this.projectDocumentationService.update(copy).subscribe((res: ProjectDocumentation) => {
                this.eventManager.broadcast({ name: 'projectDocumentationRevertModification', content: 'OK'});
            });
        });

        this.historyDocumentationService.delete(revertTo).subscribe(response => {
            // this.eventManager.broadcast({
            //     name: 'documentationHistoryListModification',
            //     content: 'Deleted an documentationHistory'
            // });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-project-delete-popup',
    template: ''
})
export class ProjectDocumentationRevertPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private projectPopupService: ProjectPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.modalRef = this.projectPopupService
                .openForRevert(ProjectDocumentationRevertDialogComponent, params);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
/**
 * Created by Chrono on 03.06.2017.
 */
