import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Attachment } from './attachment.model';
import { DateUtils } from 'ng-jhipster';
import {AbstractService} from "../abstract/abstract.service";

@Injectable()
export class AttachmentService extends AbstractService<Attachment>{

    protected resourceUrl = 'api/attachments';
    constructor(protected http: Http, protected dateUtils: DateUtils) {
        super(http,dateUtils)
    }
    queryBy(req?: any,urlAdd ?:string): Observable<Response> {
        let options = this.createRequestOption(req);
        return this.http.get(this.resourceUrl+"/"+urlAdd, options)
            .map((res: any) => this.convertResponse(res))
            ;
    }

    downloadFileById(id) {
          return this.http.get(`${'api/attachmentFile'}/${id}`)
    }

}

