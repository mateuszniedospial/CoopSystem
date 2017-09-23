import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { DocumentationCatalogue } from './documentation-catalogue.model';
import { DocumentationCataloguePopupService } from './documentation-catalogue-popup.service';
import { DocumentationCatalogueService } from './documentation-catalogue.service';

@Component({
    selector: 'jhi-documentation-catalogue-delete-dialog',
    templateUrl: './documentation-catalogue-delete-dialog.component.html'
})
export class DocumentationCatalogueDeleteDialogComponent {

    documentationCatalogue: DocumentationCatalogue;

    constructor(
        private documentationCatalogueService: DocumentationCatalogueService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear () {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete (id: number) {
        this.documentationCatalogueService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'documentationCatalogueListModification',
                content: 'Deleted an documentationCatalogue'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-documentation-catalogue-delete-popup',
    template: ''
})
export class DocumentationCatalogueDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private documentationCataloguePopupService: DocumentationCataloguePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.modalRef = this.documentationCataloguePopupService
                .open(DocumentationCatalogueDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
