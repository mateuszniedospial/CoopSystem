import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { ProjectDocumentation } from './project-documentation.model';
import { ProjectDocumentationPopupService } from './project-documentation-popup.service';
import { ProjectDocumentationService } from './project-documentation.service';
import {DocumentationHistoryService} from "../documentation-history/documentation-history.service";

@Component({
    selector: 'jhi-project-documentation-delete-dialog',
    templateUrl: './project-documentation-delete-dialog.component.html'
})
export class ProjectDocumentationDeleteDialogComponent {

    projectDocumentation: ProjectDocumentation;

    constructor(
        private projectDocumentationService: ProjectDocumentationService,
        private documentationHistoryService: DocumentationHistoryService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear () {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete (id: number) {
        this.documentationHistoryService.queryPreviousVersionsOfDocument(id).subscribe( response => {
            for(let docHistory of response.json()) {
                this.documentationHistoryService.delete(docHistory.id).subscribe(response => {});
            }
            this.projectDocumentationService.delete(id).subscribe(response => {
                this.eventManager.broadcast({
                    name: 'projectDocumentationDeleteModification',
                    content: 'Deleted an projectDocumentation'
                });
                this.activeModal.dismiss(true);
            });
        });


    }
}

@Component({
    selector: 'jhi-project-documentation-delete-popup',
    template: ''
})
export class ProjectDocumentationDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private projectDocumentationPopupService: ProjectDocumentationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.modalRef = this.projectDocumentationPopupService
                .open(ProjectDocumentationDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
