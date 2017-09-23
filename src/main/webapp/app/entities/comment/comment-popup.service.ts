import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Comment } from './comment.model';
import {Task} from "../task/task.model";
import {JUser} from "../j-user/j-user.model";
@Injectable()
export class CommentPopupService {
    private isOpen = false;
    constructor (
        private modalService: NgbModal,
        private router: Router,
    ) {}

    openOfficial(component: Component, params:any)
    : NgbModalRef {
        if (this.isOpen) {
                return;
        }
        this.isOpen = true;
        var c : Comment = new Comment();
        c.createdDate = new Date();
        c.task = new Task();
        c.task.id = params.taskID;;
        c.author = new JUser();
        c.author.id = params['authorid']
        return this.commentModalRef(component, c);
    }
    commentModalRef(component: Component, comment: Comment): NgbModalRef {
        let modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.comment = comment;
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
