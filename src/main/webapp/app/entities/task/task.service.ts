import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Task } from './task.model';
import {DateUtils, EventManager} from 'ng-jhipster';
import {AbstractService} from "../abstract/abstract.service";

@Injectable()
export class TaskService extends AbstractService<Task>{

    protected resourceUrl = 'api/tasks';
    private jGroupTasksResourceUrl = 'api/tasks/j-groups';
    private jUserTasksResourceUrl = 'api/tasks/j-users';
    private projectTasksResourceUrl = 'api/tasks/byProject';

    constructor(protected http: Http, protected dateUtils: DateUtils, private eventManager: EventManager) {super(http,dateUtils);  }

    create(task: Task): Observable<Task> {
        let copy: Task = Object.assign({}, task);
        copy.createdDate = new Date();
        copy.modifyDate = new Date();
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            this.eventManager.broadcast({ name: 'taskCreated', content: res.json()});
            return res.json();

        });
    }

    update(task: Task): Observable<Task> {
        let copy: Task = Object.assign({}, task);
        copy.modifyDate = new Date();
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            this.eventManager.broadcast({ name: 'taskModification', content: res.json()});
            return res.json();
        });
    }

    updateEstimate(task: Task){
        let copy: Task = Object.assign({}, task);
        copy.modifyDate = new Date();
        return this.http.put(this.resourceUrl+"/estimate", copy).map((res: Response) => {
            this.eventManager.broadcast({ name: 'taskModification', content: res.json()});
            return res.json();
        });
    }

    findProjectsTasks(id: number): Observable<Response>{
        return this.http.get(`${this.projectTasksResourceUrl}/${id}`).map((res: any) => this.convertResponse(res));
    }

    queryByParenId(req?: any): Observable<Response> {
        return this.queryURL(req.query,"/byParentId/");

    }

    queryJGroupTasks(id: number): Observable<Response> {
        return this.http.get(`${this.jGroupTasksResourceUrl}/${id}`).map((res: any) => this.convertResponse(res));
    }

    queryJUserTasks(id: number): Observable<Response> {
        return this.http.get(`${this.jUserTasksResourceUrl}/${id}`).map((res: any) => this.convertResponse(res));
    }

    queryLastSprintTaskByGroupId(id: number) {
        return this.queryURL(id,"/getLastSprintTaskByGroupId/");
    }

    queryTasksBySprintID(id: number) {
        return this.queryURL(id,"/bySprintId/");
    }

    private queryURL(param: number, url:string) {
        let options = this.createRequestOption({'query': param});
        return this.http.get(this.resourceUrl + url, options)
            .map((res: any) => this.convertResponse(res));
    }

}
