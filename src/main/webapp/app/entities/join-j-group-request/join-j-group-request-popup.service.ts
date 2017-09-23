import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { JoinJGroupRequest } from './join-j-group-request.model';
import { JoinJGroupRequestService } from './join-j-group-request.service';
import {JGroup} from "../j-group/j-group.model";
import {JUser} from "../j-user/j-user.model";
@Injectable()
export class JoinJGroupRequestPopupService {
    private isOpen = false;
    constructor (
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private joinJGroupRequestService: JoinJGroupRequestService

    ) {}

    open (component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.joinJGroupRequestService.find(id).subscribe(joinJGroupRequest => {
                joinJGroupRequest.createdDate = this.datePipe.transform(joinJGroupRequest.createdDate, 'yyyy-MM-ddThh:mm');
                this.joinJGroupRequestModalRef(component, joinJGroupRequest);
            });
        } else {
            return this.joinJGroupRequestModalRef(component, new JoinJGroupRequest());
        }
    }

    openCreate(component: Component, params: any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        let request:JoinJGroupRequest = new JoinJGroupRequest();
        request.jgroup = new JGroup();
        request.jgroup.id = params['jgroupid'];
        request.whoRequested = new JUser();
        request.whoRequested.id = params['whoRequested'];

        return this.joinJGroupRequestModalRef(component, request);
    }

    joinJGroupRequestModalRef(component: Component, joinJGroupRequest: JoinJGroupRequest): NgbModalRef {
        let modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.joinJGroupRequest = joinJGroupRequest;
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
