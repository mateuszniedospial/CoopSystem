import { Injectable, Component } from '@angular/core';
import {Router, Params} from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { Task } from './task.model';
import { TaskService } from './task.service';
import {JGroup} from "../j-group/j-group.model";
@Injectable()
export class TaskPopupService {
    private isOpen = false;
    constructor (
        private modalService: NgbModal,
        private router: Router,

    ) {}

    openOfficial(component: Component, params?: Params) {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;
        let task:Task = new Task();
        if(params['jGroupId']){
            let jGroup:JGroup = new JGroup();
            jGroup.id = params['jGroupId'];
            task.jgroup = jGroup;
            let parent:Task = new Task();
            parent.id = params['parentId'];
            task.parent = parent;
        }
        return this.taskModalRef(component, task);

    }

    taskModalRef(component: Component, task: Task): NgbModalRef {
        let modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.task = task;
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
