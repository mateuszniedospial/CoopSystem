import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DocumentationCatalogue } from './documentation-catalogue.model';
import { DocumentationCatalogueService } from './documentation-catalogue.service';
import {Project} from "../project/project.model";
@Injectable()
export class DocumentationCataloguePopupService {
    private isOpen = false;
    constructor (
        private modalService: NgbModal,
        private router: Router,
        private documentationCatalogueService: DocumentationCatalogueService

    ) {}

    open (component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.documentationCatalogueService.find(id).subscribe(documentationCatalogue => {
                this.documentationCatalogueModalRef(component, documentationCatalogue);
            });
        } else {
            return this.documentationCatalogueModalRef(component, new DocumentationCatalogue());
        }
    }

    openCreate(component: Component, params:any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;
        var folder:DocumentationCatalogue = new DocumentationCatalogue();
        folder.project = new Project();
        folder.project.id = params['projectid']
        return this.documentationCatalogueModalRef(component, folder);
    }

    documentationCatalogueModalRef(component: Component, documentationCatalogue: DocumentationCatalogue): NgbModalRef {
        let modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.documentationCatalogue = documentationCatalogue;
        modalRef.result.then(result => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }


}
