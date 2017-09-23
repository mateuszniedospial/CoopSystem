import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JGroupImg } from './j-group-img.model';
import { JGroupImgService } from './j-group-img.service';
import {JGroup} from "../j-group/j-group.model";
@Injectable()
export class JGroupImgPopupService {
    private isOpen = false;
    constructor (
        private modalService: NgbModal,
        private router: Router,
        private jGroupImgService: JGroupImgService

    ) {}

    open (component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.jGroupImgService.find(id).subscribe(jGroupImg => {
                this.jGroupImgModalRef(component, jGroupImg);
            });
        } else {
            return this.jGroupImgModalRef(component, new JGroupImg());
        }
    }

    openCreate(component: Component, params): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if(params['imgId']){
            this.jGroupImgService.find(params['imgId']).subscribe(jGroupImg => {
                let jGroup:JGroup = new JGroup();
                jGroup.id = params['jgroupid'];
                jGroupImg.jgroup = jGroup;
                this.jGroupImgModalRef(component, jGroupImg);
            });
        } else {
            let image:JGroupImg = new JGroupImg();
            let jGroup:JGroup = new JGroup();
            jGroup.id = params['jgroupid'];
            image.jgroup = jGroup;
            this.jGroupImgModalRef(component, image);
        }
    }

    jGroupImgModalRef(component: Component, jGroupImg: JGroupImg): NgbModalRef {
        let modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.jGroupImg = jGroupImg;
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
