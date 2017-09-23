import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { JUserImg } from './j-user-img.model';
import { DateUtils } from 'ng-jhipster';
import {AbstractService} from "../abstract/abstract.service";

@Injectable()
export class JUserImgService extends AbstractService<JUserImg>{

    protected resourceUrl = 'api/j-user-imgs';
    private jUserImgOfSpecificJUser = 'api/j-user-imgs/j-user'
    private resourceUrl1= 'api/j-user-imgByJuserId}'

    constructor(protected http: Http, protected dateUtils: DateUtils) {super(http,dateUtils); }


    query(req?: any): Observable<Response> {
        let options = this.createRequestOption(req);
        if(req){
            if(req.juserId){
                return this.http.get(this.resourceUrl1, options);
            }else {
                return this.http.get(this.resourceUrl, options);
            }
        } else {
            return this.http.get(this.resourceUrl, options);
        }
    }

    queryJImgOfJUser(id: number): Observable<JUserImg> {
        return this.http.get(`${this.jUserImgOfSpecificJUser}/${id}`).map((res: Response) => {
            return res.json();
        });
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
            if(req.juserId) params.set('juserId', req.juserId);

            options.search = params;
        }
        return options;
    }
}
