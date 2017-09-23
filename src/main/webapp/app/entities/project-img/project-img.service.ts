import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { DateUtils } from 'ng-jhipster';
import {AbstractService} from "../abstract/abstract.service";
import { ProjectImg } from './project-img.model';
@Injectable()
export class ProjectImgService extends AbstractService<ProjectImg>{

    protected resourceUrl = 'api/project-imgs';
    private imgOfProjectUrl = 'api/project-img/project';

    constructor(protected http: Http, protected dateUtils: DateUtils) {super(http,dateUtils); }

    findImgOfProject(id: number): Observable<ProjectImg> {
        return this.http.get(`${this.imgOfProjectUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

}
