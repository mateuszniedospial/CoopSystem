import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { WorkLog } from './work-log.model';
import {Task} from "../task/task.model";
import {JUser} from "../j-user/j-user.model";
@Injectable()
export class WorkLogPopupService {
    private isOpen = false;
    constructor (
        private modalService: NgbModal,
        private router: Router,
    ) {}

    openOfficial(component: Component, params:any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;
        let wL : WorkLog = new WorkLog();
        wL.createdDate = new Date();
        wL.task = new Task();
        wL.task.id = params.taskID;;
        wL.juser = new JUser();
        wL.juser.id = params['authorid']
        return this.workLogModalRef(component, wL);
    }
    workLogModalRef(component: Component, workLog: WorkLog): NgbModalRef {
        let modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.workLog = workLog;
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
