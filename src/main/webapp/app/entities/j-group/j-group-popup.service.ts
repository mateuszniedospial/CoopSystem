import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { JGroup } from './j-group.model';
import { JGroupService } from './j-group.service';
import {JUser} from "../j-user/j-user.model";
import {Project} from "../project/project.model";
import {ProjectService} from "../project/project.service";
@Injectable()
export class JGroupPopupService {
    private isOpen = false;
    constructor (
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private jGroupService: JGroupService,
        private projectService: ProjectService,

    ) {}

    open (component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.jGroupService.find(id).subscribe(jGroup => {
                jGroup.createdDate = this.datePipe.transform(jGroup.createdDate, 'yyyy-MM-ddThh:mm');
                jGroup.modifyDate = this.datePipe.transform(jGroup.modifyDate, 'yyyy-MM-ddThh:mm');
                this.jGroupModalRef(component, jGroup);
            });
        } else {
            return this.jGroupModalRef(component, new JGroup());
        }
    }

    openCreate(component: Component, params): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        this.projectService.find(params['projectid']).subscribe(project => {
           let jgroup:JGroup = new JGroup();
           let user:JUser = new JUser();
           let gUsers:JUser[] = [];
           user.id = params['userid'];
           gUsers.push(user);
           jgroup.jusers = gUsers;
           this.jGroupModalRefWithProject(component, jgroup, project, user);
        });
    }

    leave(component: Component, params): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        this.jGroupService.find(params['groupId']).subscribe(jGroup => {
            let usersOfGroup:JUser[] = jGroup.jusers;
            let leaverId:number = params['leaverId'];
            let modUsers:JUser[] = [];
            for(let user of usersOfGroup){
                if(user.id != leaverId){
                    modUsers.push(user);
                }
            }
            jGroup.jusers = modUsers;

            // jGroup.createdDate = this.datePipe.transform(jGroup.createdDate, 'yyyy-MM-ddThh:mm');
            jGroup.modifyDate = new Date();
            this.jGroupModalRef(component, jGroup);
        });
    }

    jGroupModalRef(component: Component, jGroup: JGroup): NgbModalRef {
        let modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.jGroup = jGroup;
        modalRef.componentInstance.project = null;
        modalRef.result.then(result => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }

    jGroupModalRefWithProject(component: Component, jGroup: JGroup, project: Project, user: JUser): NgbModalRef {
        let modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.jGroup = jGroup;
        modalRef.componentInstance.project = project;
        modalRef.componentInstance.userToAdd = user;
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
