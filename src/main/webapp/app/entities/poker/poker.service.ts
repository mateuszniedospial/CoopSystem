import { Injectable } from '@angular/core';
import { Http, Response} from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { DateUtils } from 'ng-jhipster';
import {AbstractService} from "../abstract/abstract.service";

import { Poker } from './poker.model';
@Injectable()
export class PokerService extends AbstractService<Poker> {

    protected resourceUrl = 'api/pokers';

    constructor(protected http: Http, protected dateUtils: DateUtils) {super(http,dateUtils); }

    queryByTaskId(taskId): Observable<Response> {
        let options = this.createRequestOption({'query':taskId});
        return this.http.get(this.resourceUrl+'/byTaskId', options);
    }

}
