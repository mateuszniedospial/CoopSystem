import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, DataUtils } from 'ng-jhipster';

import { Comment } from './comment.model';
import { CommentPopupService } from './comment-popup.service';
import { CommentService } from './comment.service';
import { Task, TaskService } from '../task';
@Component({
    selector: 'comment-official-dialog',
    templateUrl: './comment-official-dialog.component.html'
})
export class CommentOfficialComponent implements OnInit {

    comment: Comment;
    authorities: any[];
    isSaving: boolean;

    tasks: Task[];
    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: DataUtils,
        private alertService: AlertService,
        private commentService: CommentService,
        private taskService: TaskService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;

        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.taskService.query().subscribe(
            (res: Response) => { this.tasks = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData($event, comment, field, isImage) {
        if ($event.target.files && $event.target.files[0]) {
            let $file = $event.target.files[0];
            if (isImage && !/^image\//.test($file.type)) {
                return;
            }
            this.dataUtils.toBase64($file, (base64Data) => {
                comment[field] = base64Data;
                comment[`${field}ContentType`] = $file.type;
            });
        }
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.comment.id !== undefined) {
            this.commentService.update(this.comment)
                .subscribe((res: Comment) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.commentService.create(this.comment)
                .subscribe((res: Comment) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: Comment) {
        this.eventManager.broadcast({ name: 'commentListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError (error) {
        this.isSaving = false;
        this.onError(error);
    }

    private onError (error) {
        this.alertService.error(error.message, null, null);
    }

    trackTaskById(index: number, item: Task) {
        return item.id;
    }
}

@Component({
    selector: 'comment-official-popup',
    template: ''
})
export class CommentOfficialPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private commentPopupService: CommentPopupService,
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
                this.modalRef = this.commentPopupService
                    .openOfficial(CommentOfficialComponent, params);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
