import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { Project } from './project.model';
import { ProjectService } from './project.service';
import {ProjectDocumentationService} from "../project-documentation/project-documentation.service";
import {ProjectDocumentation} from "../project-documentation/project-documentation.model";
import {DocumentationHistory} from "../documentation-history/documentation-history.model";
import {DocumentationHistoryService} from "../documentation-history/documentation-history.service";
@Injectable()
export class ProjectPopupService {
    private isOpen = false;
    constructor (
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private projectService: ProjectService,
        private projectDocumentationService: ProjectDocumentationService,
        private historyDocumentationService: DocumentationHistoryService

    ) {}

    open (component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.projectService.find(id).subscribe(project => {
                // project.createdDate = this.datePipe.transform(project.createdDate, 'yyyy-MM-ddThh:mm');
                project.modifyDate = new Date();
                this.projectModalRef(component, project);
            });
        } else {
            return this.projectModalRef(component, new Project());
        }
    }

    openForRevert (component: Component, params: any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;



        this.projectDocumentationService.find(params['doc']).subscribe(doc => {
            doc.createdDate = this.datePipe.transform(doc.createdDate, 'yyyy-MM-ddThh:mm');
            doc.modifyDate = this.datePipe.transform(doc.modifyDate, 'yyyy-MM-ddThh:mm');
            this.projectDocumentationModalRef(component, doc, params['revertTo']);
        });
    }

    projectDocumentationModalRef(component: Component, doc: ProjectDocumentation, revertTo:any): NgbModalRef {
        let modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.doc = doc;
        modalRef.componentInstance.revertTo = revertTo;
        modalRef.result.then(result => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }

    projectModalRef(component: Component, project: Project): NgbModalRef {
        let modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.project = project;
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
