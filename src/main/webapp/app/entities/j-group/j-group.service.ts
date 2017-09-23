import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { JGroup } from './j-group.model';
import { DateUtils } from 'ng-jhipster';
import {AbstractService} from "../abstract/abstract.service";

@Injectable()
export class JGroupService extends AbstractService<JGroup>{

    protected resourceUrl = 'api/j-groups';
    private userGroupsResourceUrl = 'api/j-groups/j-users';

    constructor(protected http: Http, protected dateUtils: DateUtils) {super(http,dateUtils); }

    querySpecificUserGroups(id: number): Observable<Response> {
        return this.http.get(`${this.userGroupsResourceUrl}/${id}`).map((res: any) => this.convertResponse(res));
    }

    queryJUserId(req?: any): Observable<Response> {
        let options = this.createRequestOption(req);
        return this.http.get(this.resourceUrl+"/byJUserId/", options)
            .map((res: any) => this.convertResponse(res))
            ;
    }

}
