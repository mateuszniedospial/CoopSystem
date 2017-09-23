import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import {DateUtils, EventManager} from 'ng-jhipster';
import {QueryParameter, CustomCheckbox} from "./search.component";
@Injectable()
export class SearchService {

    private resourceUrl = 'api/search';

    constructor(private http: Http, private dateUtils: DateUtils, private eventManager: EventManager) { }

    query(queryParameter:QueryParameter, parameterFlag: CustomCheckbox): Observable<Response> {
        let options: BaseRequestOptions = new BaseRequestOptions();
        let params: URLSearchParams = new URLSearchParams();
        this.addParamIfRequired(true, queryParameter.jGroupId, 'jGroupId', params);
        this.addParamIfRequired(parameterFlag.isTitle, queryParameter.title, 'title', params);
        this.addParamIfRequired(parameterFlag.isId, queryParameter.id, 'id', params);
        this.addParamIfRequired(parameterFlag.isVersion, queryParameter.version, 'version', params);
        this.addParamIfRequired(parameterFlag.isDescription, queryParameter.description, 'description', params);
        this.addParamIfRequired(parameterFlag.isEnviroment, queryParameter.enviroment, 'enviroment', params);
        this.addParamIfRequired(parameterFlag.isStatus, queryParameter.status, 'status', params);
        this.addParamIfRequired(parameterFlag.isComment, queryParameter.comment, 'comment', params);
        this.addParamIfRequired(parameterFlag.isPrioritu, queryParameter.priority, 'priority', params);
        this.addParamIfRequired(parameterFlag.isProject, queryParameter.project, 'project', params);
        this.addParamIfRequired(parameterFlag.isAssigne, queryParameter.assignee, 'assignee', params);
        this.addParamIfRequired(parameterFlag.isAfterCreated, queryParameter.createDate, 'afterCreate', params);
        this.addParamIfRequired(parameterFlag.isBeforeCreated, queryParameter.createDate, 'beforeCreate', params);
        this.addParamIfRequired(parameterFlag.isAfterModfied, queryParameter.modifiedDate, 'afterModify', params);
        this.addParamIfRequired(parameterFlag.isBeforeModified, queryParameter.modifiedDate, 'beforeModify', params);
        options.search = params;
        return this.http.get(this.resourceUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private addParamIfRequired(required:boolean, paramValue:any, paramsName:string, params: URLSearchParams){
        if (required){
            params.set(paramsName, paramValue);
        }
    }

    private convertResponse(res: any): any {
        let jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            jsonResponse[i].createdDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse[i].createdDate);
            jsonResponse[i].modifyDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse[i].modifyDate);
        }
        res._body = jsonResponse;
        return res;
    }
}
