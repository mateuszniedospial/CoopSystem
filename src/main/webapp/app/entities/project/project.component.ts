import { Component, OnInit, OnDestroy } from '@angular/core';
import { Response } from '@angular/http';
import { Subscription } from 'rxjs/Rx';
import { EventManager, AlertService, DataUtils } from 'ng-jhipster';

import { Project } from './project.model';
import { ProjectService } from './project.service';
import { Principal } from '../../shared';
import {ProjectImg} from "../project-img/project-img.model";
import {ProjectImgService} from "../project-img/project-img.service";

@Component({
    selector: 'jhi-project',
    templateUrl: './project.component.html',
    styleUrls: [
        'project-view.css'
    ]
})
export class ProjectComponent implements OnInit, OnDestroy {

    projects: Project[];
    projectsImgMap:{[key:string]:ProjectImg;} = {};

    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private projectService: ProjectService,
        private projectImgService: ProjectImgService,
        private alertService: AlertService,
        private dataUtils: DataUtils,
        private eventManager: EventManager,
        private principal: Principal
    ) {
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInProjects();
    }

    loadAll() {
        this.projectService.query().subscribe(
            (res: Response) => {
                this.projects = res.json();
                for(let project of res.json()){
                    this.queryImgOfProject(project.id);
                }
            },
            (res: Response) => this.onError(res.json())
        );
    }

    queryImgOfProject(id){
        this.projectImgService.findImgOfProject(id).subscribe(img => {
            this.projectsImgMap[id] = img;
        });
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId (index: number, item: Project) {
        return item.id;
    }



    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    registerChangeInProjects() {
        this.eventSubscriber = this.eventManager.subscribe('projectListModification', (response) => this.loadAll());
    }


    private onError (error) {
        this.alertService.error(error.message, null, null);
    }
}
