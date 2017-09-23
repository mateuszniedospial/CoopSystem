import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { TaskDescription } from './task-description.model';
import { DateUtils } from 'ng-jhipster';
import {AbstractService} from "../abstract/abstract.service";

@Injectable()
export class TaskDescriptionService extends AbstractService<TaskDescription> {

    protected resourceUrl = 'api/task-descriptions';
    private taskDescriptionOfSpecificTask = "api/task-descriptions/tasks"

    constructor(protected http: Http, protected dateUtils: DateUtils) {super(http,dateUtils); }

    queryTaskDescriptionOfSpecificTask(id: number): Observable<TaskDescription> {
        return this.http.get(`${this.taskDescriptionOfSpecificTask}/${id}`).map((res: Response) => {
            let jsonResponse = res.json();
            jsonResponse.createdDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse.createdDate);
            jsonResponse.modifyDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse.modifyDate);
            return jsonResponse;
        });
    }


    queryByTaskId(req?: any): Observable<Response> {
        let options = this.createRequestOption(req);
        return this.http.get(this.resourceUrl+"/byTaskId", options).map((res: any) => this.convertResponse(res));
    }

}
