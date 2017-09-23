/**
 * Created by Chrono on 11.06.2017.
 */
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, AlertService, DataUtils } from 'ng-jhipster';

import { Project } from './project.model';
import { ProjectService } from './project.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import {JUserService} from "../j-user/j-user.service";
import {JUser} from "../j-user/j-user.model";
import {ProjectImg} from "../project-img/project-img.model";
import {ProjectImgService} from "../project-img/project-img.service";
import {LoginService} from "../../shared/login/login.service";

@Component({
    selector: 'projects-main',
    templateUrl: './projects-main.component.html',
    styleUrls: [
        'project-view.css'
    ]
})
export class ProjectsMainComponent implements OnInit, OnDestroy {

    jUser: JUser;

    projects: Project[];
    projectsImgMap:{[key:string]:ProjectImg;} = {};

    eventSubscriber: Subscription;

    constructor(
        private projectService: ProjectService,
        private projectImgService: ProjectImgService,
        private jUserService: JUserService,
        private alertService: AlertService,
        private loginService: LoginService,
        private dataUtils: DataUtils,
        private eventManager: EventManager,
        private principal: Principal,
        private router: Router
    ) {
    }

    ngOnInit() {
       this.initializeDataGathering();
       this.listenToEvents();
    }

    listenToEvents(){
        this.registerChangeInProjects();
        this.registerChangeInProjectsImg();
    }

    initializeDataGathering(){
        this.principal.identity().then(user => {
            this.jUserService.queryJUser(user.id).subscribe(juser => {
                this.jUser = juser;
                this.loadProjects(juser.id)
            })
        });
    }

    loadProjects(id) {
        this.projectService.querySpecificUserProjects(id).subscribe(
            (res: Response) => {
                this.projects = res.json();
                for(let project of res.json()){
                    this.loadProjectImg(project.id);
                }
            },
            (res: Response) => this.onError(res.json())
        );
    }

    loadProjectImg(id){
        this.projectImgService.findImgOfProject(id).subscribe(img => {
            this.projectsImgMap[id] = img;
        });
    }

    logout(){
        this.loginService.logout();
        this.router.navigate(['']);
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    registerChangeInProjects() {
        this.eventSubscriber = this.eventManager.subscribe('projectListModification', (response) => this.loadProjects(this.jUser.id));
    }

    registerChangeInProjectsImg(){
        this.eventSubscriber = this.eventManager.subscribe('projectImgListModification', (response) => this.loadProjects(this.jUser.id));
    }

    private onError (error) {
        this.alertService.error(error.message, null, null);
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }
}
