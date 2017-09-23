import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { DocumentationCatalogue } from './documentation-catalogue.model';
import {AbstractService} from "../abstract/abstract.service";
import {DateUtils} from "ng-jhipster";
@Injectable()
export class DocumentationCatalogueService extends AbstractService<DocumentationCatalogue>{

    protected resourceUrl = 'api/documentation-catalogues';
    private findByLabel = 'api/documentation-catalogues/byLabel';

    constructor(protected http: Http, protected dateUtils: DateUtils) {
        super(http,dateUtils); }


    queryByLabel(label: string): Observable<DocumentationCatalogue> {
        return this.http.get(`${this.findByLabel}/${label}`).map((res: Response) => {
            return res.json();
        });
    }

}
