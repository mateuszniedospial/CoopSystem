import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { PokerVotes } from './poker-votes.model';
@Injectable()
export class PokerVotesService {

    private resourceUrl = 'api/poker-votes';

    constructor(private http: Http) { }

    create(pokerVotes: PokerVotes): Observable<PokerVotes> {
        let copy: PokerVotes = Object.assign({}, pokerVotes);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }


    update(pokerVotes: PokerVotes): Observable<PokerVotes> {
        let copy: PokerVotes = Object.assign({}, pokerVotes);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<PokerVotes> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<Response> {
        let options = this.createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
        ;
    }

    queryByPokerId(pokerId): Observable<Response> {
        let options = this.createRequestOption({"query": pokerId});
        return this.http.get(this.resourceUrl+"/byPokerId", options)
            ;
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    deleteByPokerId(pokerId: number): Observable<Response> {
        let options = this.createRequestOption({"query": pokerId});
        return this.http.delete(this.resourceUrl+"/byPokerId", options);
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

            options.search = params;
        }
        return options;
    }
}
