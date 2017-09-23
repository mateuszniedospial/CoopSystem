import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { TaskHistory } from './task-history.model';
import { DateUtils } from 'ng-jhipster';
import {AbstractService} from "../abstract/abstract.service";

@Injectable()
export class TaskHistoryService extends AbstractService<TaskHistory> {

    protected resourceUrl = 'api/task-histories';
    private getAllHistoriesOfTasksModifiedByUserUrl = 'api/task-histories/j-users'
    private getAllHistoriesOfJGroupTasks = 'api/task-histories/j-group'

    constructor(protected http: Http, protected dateUtils: DateUtils) {super(http,dateUtils); }

    queryHistoriesOfTasksModifiedByUser(id: number): Observable<Response>{
        return this.http.get(`${this.getAllHistoriesOfTasksModifiedByUserUrl}/${id}`).map((res: any) => this.convertResponse(res));
    }

    queryHistoriesOfJGroupTasks(id: number): Observable<Response>{
        return this.http.get(`${this.getAllHistoriesOfJGroupTasks}/${id}`).map((res: any) => this.convertResponse(res));
    }

    queryByTaskId(req?: any): Observable<Response> {
        let options = this.createRequestOption(req);
        return this.http.get(this.resourceUrl+"/byTaskId/", options)
            .map((res: any) => this.convertResponse(res))
            ;
    }
}
