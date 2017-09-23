import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { ProjectDocumentation } from './project-documentation.model';
import { ProjectDocumentationService } from './project-documentation.service';
import {Project} from "../project/project.model";
import {JUser} from "../j-user/j-user.model";
@Injectable()
export class ProjectDocumentationPopupService {
    private isOpen = false;
    constructor (
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private projectDocumentationService: ProjectDocumentationService

    ) {}

    open (component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.projectDocumentationService.find(id).subscribe(projectDocumentation => {
                projectDocumentation.createdDate = this.datePipe.transform(projectDocumentation.createdDate, 'yyyy-MM-ddThh:mm');
                projectDocumentation.modifyDate = this.datePipe.transform(projectDocumentation.modifyDate, 'yyyy-MM-ddThh:mm');
                this.projectDocumentationModalRef(component, projectDocumentation);
            });
        } else {
            return this.projectDocumentationModalRef(component, new ProjectDocumentation());
        }
    }

    openCreate(component: Component, params:any): NgbModalRef{
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;
        var doc:ProjectDocumentation = new ProjectDocumentation();
        // doc.createdDate = new Date();
        // doc.modifyDate = new Date();
        doc.project = new Project();
        doc.project.id = params['projectid'];
        doc.lastAuthor = new JUser();
        doc.lastAuthor.id = params['authorid'];

        return this.projectDocumentationModalRef(component, doc);
    }

    projectDocumentationModalRef(component: Component, projectDocumentation: ProjectDocumentation): NgbModalRef {
        let modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.projectDocumentation = projectDocumentation;
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
