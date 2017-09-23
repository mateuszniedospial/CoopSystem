import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { DateUtils } from 'ng-jhipster';
import {AbstractService} from "../abstract/abstract.service";

import { JGroupImg } from './j-group-img.model';
@Injectable()
export class JGroupImgService extends AbstractService<JGroupImg> {

    protected resourceUrl = 'api/j-group-imgs';
    private jGroupImgOfSpecificJGroup = 'api/j-group-img/j-group';

    constructor(protected http: Http,protected dateUtils: DateUtils) {super(http,dateUtils); }

    queryJImg(id: number): Observable<JGroupImg> {
        return this.http.get(`${this.jGroupImgOfSpecificJGroup}/${id}`).map((res: Response) => {
            return res.json();
        });
    }
}
