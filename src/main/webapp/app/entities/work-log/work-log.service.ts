import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { WorkLog } from './work-log.model';
import { DateUtils } from 'ng-jhipster';
import {AbstractService} from "../abstract/abstract.service";

@Injectable()
export class WorkLogService  extends AbstractService<WorkLog> {

    protected resourceUrl = 'api/work-logs';

    constructor(protected http: Http, protected dateUtils: DateUtils) {super(http,dateUtils); }

    queryByTaskId(req?: any): Observable<Response> {
        let options = this.createRequestOption(req);
        return this.http.get(this.resourceUrl+"/"+"byTaskId/", options)
            .map((res: any) => this.convertResponse(res))
            ;
    }

    protected convertResponse(res: any): any {
        let jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            jsonResponse[i].createdDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse[i].createdDate);
            jsonResponse[i].modifyDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse[i].modifyDate);
            jsonResponse[i].startWork = this.dateUtils
                .convertDateTimeFromServer(jsonResponse[i].startWork);
            jsonResponse[i].stopWork = this.dateUtils
                .convertDateTimeFromServer(jsonResponse[i].stopWork);
        }
        res._body = jsonResponse;
        return res;
    }

}
