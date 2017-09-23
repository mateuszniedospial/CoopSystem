import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import { Report } from './report.model';
import { DateUtils } from 'ng-jhipster';
import {AbstractService} from "../abstract/abstract.service";
@Injectable()
export class ReportService extends AbstractService<Report> {

    protected resourceUrl = 'api/reports';

    constructor(protected http: Http, protected dateUtils: DateUtils) {super(http,dateUtils); }

    queryBySprintId(id:number){
        let options = this.createRequestOption({'query':id});
        return this.http.get(this.resourceUrl+'/getBySprintId', options).map((res: any) => this.convertResponse(res));
    }
}
