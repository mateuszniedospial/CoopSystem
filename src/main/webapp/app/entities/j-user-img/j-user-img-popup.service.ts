import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JUserImg } from './j-user-img.model';
import { JUserImgService } from './j-user-img.service';
import {JUser} from "../j-user/j-user.model";
@Injectable()
export class JUserImgPopupService {
    private isOpen = false;
    constructor (
        private modalService: NgbModal,
        private router: Router,
        private jUserImgService: JUserImgService

    ) {}

    open (component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.jUserImgService.find(id).subscribe(jUserImg => {
                this.jUserImgModalRef(component, jUserImg);
            });
        } else {
            return this.jUserImgModalRef(component, new JUserImg());
        }
    }

    openCreate (component: Component, params): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (params['imgId']) {
            this.jUserImgService.find(params['imgId']).subscribe(jUserImg => {
                let user:JUser = new JUser();
                user.id = params['userid'];
                jUserImg.juser = user;
                this.jUserImgModalRef(component, jUserImg);
            });
        } else {
            let newImg:JUserImg = new JUserImg();
            let user:JUser = new JUser();
            user.id = params['userid'];
            newImg.juser = user;
            return this.jUserImgModalRef(component, newImg);
        }
    }



    jUserImgModalRef(component: Component, jUserImg: JUserImg): NgbModalRef {
        let modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.jUserImg = jUserImg;
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
