import { Injectable } from '@angular/core';
import {Http, Response, BaseRequestOptions, URLSearchParams} from '@angular/http';
import { Observable } from 'rxjs/Rx';

@Injectable()
export class AccountService  {
    constructor(private http: Http) { }

    get(): Observable<any> {
        return this.http.get('api/account').map((res: Response) => res.json());
    }
    getById(req:any): Observable<any> {
    return this.http.get('api/accountById', this.createRequestOption(req)).map((res: Response) => res.json());
}

    save(account: any): Observable<Response> {
        return this.http.post('api/account', account);
    }
    private createRequestOption(req?: any): BaseRequestOptions {
        let options: BaseRequestOptions = new BaseRequestOptions();
        if (req) {
            let params: URLSearchParams = new URLSearchParams();
            params.set('page', req.page);
            params.set('size', req.size);
            if (req.sort) {
                params.paramsMap.set('sort', req.sort);
            }
            params.set('query', req.query);
            params.set('jhiId', req.jhiId);
            options.search = params;
        }
        return options;
    }

}
