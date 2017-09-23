import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { DocumentationHistory } from './documentation-history.model';
import { DateUtils } from 'ng-jhipster';
import {AbstractService} from "../abstract/abstract.service";
@Injectable()
export class DocumentationHistoryService extends AbstractService<DocumentationHistory> {

    protected resourceUrl = 'api/documentation-histories';
    private allDocumentVersions = 'api/documentation-histories/document';

    constructor(protected http: Http, protected dateUtils: DateUtils) {super(http,dateUtils); }

    queryPreviousVersionsOfDocument(id: number): Observable<Response> {
        return this.http.get(`${this.allDocumentVersions}/${id}`).map((res: any) => this.convertResponse(res));
    }

}
