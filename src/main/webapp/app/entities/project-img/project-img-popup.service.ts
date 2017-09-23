import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ProjectImg } from './project-img.model';
import { ProjectImgService } from './project-img.service';
import {Project} from "../project/project.model";
@Injectable()
export class ProjectImgPopupService {
    private isOpen = false;
    constructor (
        private modalService: NgbModal,
        private router: Router,
        private projectImgService: ProjectImgService

    ) {}

    open (component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.projectImgService.find(id).subscribe(projectImg => {
                this.projectImgModalRef(component, projectImg);
            });
        } else {
            return this.projectImgModalRef(component, new ProjectImg());
        }
    }

    openCreate(component: Component, params): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if(params['imgid']){
            this.projectImgService.find(params['imgid']).subscribe(projectImg => {
                this.projectImgModalRef(component, projectImg);
            });
        } else {
            let img:ProjectImg = new ProjectImg();
            let project:Project = new Project();
            project.id = params['projectid'];
            img.project = project;
            return this.projectImgModalRef(component, img);
        }
    }

    projectImgModalRef(component: Component, projectImg: ProjectImg): NgbModalRef {
        let modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.projectImg = projectImg;
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
