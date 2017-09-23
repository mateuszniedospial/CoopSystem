import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Project } from './project.model';
import { DateUtils } from 'ng-jhipster';
import {TreeNode} from "primeng/components/common/api";
import {AbstractService} from "../abstract/abstract.service";
@Injectable()
export class ProjectService extends AbstractService<Project>{

    protected resourceUrl = 'api/projects';
    private forPathUrl = 'api/project';
    private jUserProjectsUrl = 'api/projects/juser';

    constructor(protected http: Http, protected dateUtils: DateUtils) {super(http,dateUtils); }

    getAllDocs(id: number) {
        return this.http.get(`${this.forPathUrl}/${id}/documentation-catalogues/asJSON`)
            .toPromise()
            .then(res => <TreeNode[]> res.json().data);
    }

    querySpecificUserProjects(id: number): Observable<Response> {
        return this.http.get(`${this.jUserProjectsUrl}/${id}`).map((res: any) => this.convertResponse(res));
    }

    queryByGroupId(groupId:number){
        let options = this.createRequestOption({'query':groupId});
        return this.http.get(this.resourceUrl+'/byJGroupId', options).map((res: any) => this.convertResponse(res));
    }

}
