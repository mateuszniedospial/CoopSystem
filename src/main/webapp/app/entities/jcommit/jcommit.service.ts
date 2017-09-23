import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Jcommit } from './jcommit.model';
import { DateUtils } from 'ng-jhipster';
import {AbstractService} from "../abstract/abstract.service";

@Injectable()
export class JcommitService extends AbstractService<Jcommit> {
    protected resourceUrl = 'api/jcommits';

    constructor(protected http: Http, protected dateUtils: DateUtils) {super(http,dateUtils); }

    queryByTaskId(id:number): Observable<Response> {
        let options = this.createRequestOption({'query':id});
        return this.http.get(this.resourceUrl+'/byTasks/', options)
            .map((res: any) => this.convertResponse(res));
    }

}
