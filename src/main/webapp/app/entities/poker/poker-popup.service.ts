import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Poker } from './poker.model';
import { PokerService } from './poker.service';
import {Task} from "../task/task.model";
import {JGroup} from "../j-group/j-group.model";
@Injectable()
export class PokerPopupService {
    private isOpen = false;
    constructor (
        private modalService: NgbModal,
        private router: Router,
        private pokerService: PokerService,
    ) {}

    open (component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.pokerService.find(id).subscribe(poker => {
                this.pokerModalRef(component, poker);
            });
        } else {
            return this.pokerModalRef(component, new Poker());
        }
    }

    openOfficial (component: Component, taskId:number, jGroupId: number): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;
        let poker = new Poker();
        let task = new Task();
        task.id = taskId;
        let jGroup = new JGroup();
        jGroup.id = jGroupId;
        poker.task = task;
        poker.jGroup = jGroup;
        return this.pokerModalRef(component, poker);
    }

    pokerModalRef(component: Component, poker: Poker): NgbModalRef {
        let modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.poker = poker;
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
