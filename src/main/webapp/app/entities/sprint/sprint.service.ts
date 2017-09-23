import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Sprint } from './sprint.model';
import { DateUtils } from 'ng-jhipster';
import {AbstractService} from "../abstract/abstract.service";

@Injectable()
export class SprintService extends AbstractService<Sprint> {

    protected resourceUrl = 'api/sprints';

    constructor(protected http: Http, protected dateUtils: DateUtils) {super(http,dateUtils); }

    find(id: number): Observable<Sprint> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            let jsonResponse = res.json();
            jsonResponse.createdDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse.createdDate);
            jsonResponse.modifyDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse.modifyDate);
            jsonResponse.startTime = this.dateUtils
                .convertDateTimeFromServer(jsonResponse.startTime);
            jsonResponse.stopTime = this.dateUtils
                .convertDateTimeFromServer(jsonResponse.stopTime);
            return jsonResponse;
        });
    }

    queryByJGroupId(req?: any): Observable<Response> {
        let options = this.createRequestOption(req);
        return this.http.get(this.resourceUrl+"/byJGroupId", options)
            .map((res: any) => this.convertResponse(res));
    }

    protected convertResponse(res: any): any {
        let jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            jsonResponse[i].createdDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse[i].createdDate);
            jsonResponse[i].modifyDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse[i].modifyDate);
            jsonResponse[i].startTime = this.dateUtils
                .convertDateTimeFromServer(jsonResponse[i].startTime);
            jsonResponse[i].stopTime = this.dateUtils
                .convertDateTimeFromServer(jsonResponse[i].stopTime);
        }
        res._body = jsonResponse;
        return res;
    }
}
