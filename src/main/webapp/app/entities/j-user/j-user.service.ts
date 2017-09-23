import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { JUser } from './j-user.model';
import { DateUtils } from 'ng-jhipster';
import {AbstractService} from "../abstract/abstract.service";
@Injectable()
export class JUserService extends AbstractService<JUser>{

    protected resourceUrl = 'api/j-users';
    private jUserByJhiUserRetrieveResourceUrl = 'api/j-users/jhi-users'

    private resourceUrl1 = 'api/j-userBjhi';
    constructor(protected http: Http, protected dateUtils: DateUtils) {super(http,dateUtils); }


    queryJUser(id: number): Observable<JUser> {
        return this.http.get(`${this.jUserByJhiUserRetrieveResourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<Response> {
        let options = this.createRequestOption(req);
       if(req){
           if(req.jhiId){
               return this.http.get(this.resourceUrl1, options);
           }else {
               return this.http.get(this.resourceUrl, options);
           }
       } else {
            return this.http.get(this.resourceUrl, options);
        }
    }

    protected createRequestOption(req?: any): BaseRequestOptions {
        let options: BaseRequestOptions = new BaseRequestOptions();
        if (req) {
            let params: URLSearchParams = new URLSearchParams();
            params.set('page', req.page);
            params.set('size', req.size);
            if (req.sort) {
                params.paramsMap.set('sort', req.sort);
            }
            params.set('query', req.query);
            if(req.jhiId)params.set('jhiId', req.jhiId);

            options.search = params;
        }
        return options;
    }
}
