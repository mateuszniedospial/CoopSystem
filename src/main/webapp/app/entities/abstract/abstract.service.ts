import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { DateUtils } from 'ng-jhipster';
@Injectable()
export abstract class AbstractService<T> {

    protected resourceUrl = '';

    constructor(protected http: Http, protected dateUtils: DateUtils) { }

    public create(entity: T): Observable<T> {
        let copy: T = Object.assign({}, entity);
        copy['createdDate'] = new Date();
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    public  update(entity: T): Observable<T> {
        let copy: T = Object.assign({}, entity);
        copy['modifyDate'] = new Date();
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    public  find(id: number): Observable<T> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            let jsonResponse = res.json();
            jsonResponse.createdDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse.createdDate);
            jsonResponse.modifyDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse.modifyDate);
            return jsonResponse;
        });
    }

    public  query(req?: any): Observable<Response> {
        let options = this.createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: any) => this.convertResponse(res))
        ;
    }


    public  delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }


    protected convertResponse(res: any): any {
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

            options.search = params;
        }
        return options;
    }

}
