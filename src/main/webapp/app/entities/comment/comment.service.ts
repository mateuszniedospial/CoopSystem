import { Injectable } from '@angular/core';
import { Http, URLSearchParams, BaseRequestOptions } from '@angular/http';

import { Comment } from './comment.model';
import { DateUtils } from 'ng-jhipster';
import {AbstractService} from "../abstract/abstract.service";

@Injectable()
export class CommentService extends AbstractService<Comment>{

    protected resourceUrl = 'api/comments';

    constructor(protected http: Http, protected dateUtils: DateUtils) {super(http,dateUtils); }

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
            params.set('taskid', req.taskid);
            options.search = params;
        }
        return options;
    }
}
