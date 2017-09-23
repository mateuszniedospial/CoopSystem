import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { JoinJGroupRequest } from './join-j-group-request.model';
import { DateUtils } from 'ng-jhipster';
import {AbstractService} from "../abstract/abstract.service";

@Injectable()
export class JoinJGroupRequestService extends AbstractService<JoinJGroupRequest> {

    protected resourceUrl = 'api/join-j-group-requests';
    private allRequestsForGroup = 'api/join-j-group-requests/jgroup';

    constructor(protected http: Http, protected dateUtils: DateUtils) {super(http,dateUtils); }

    querySpecificGroupRequests(id: number): Observable<Response> {
        return this.http.get(`${this.allRequestsForGroup}/${id}`).map((res: any) => this.convertResponse(res));
    }
}
