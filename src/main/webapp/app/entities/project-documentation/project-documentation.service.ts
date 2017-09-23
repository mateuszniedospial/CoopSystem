import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { ProjectDocumentation } from './project-documentation.model';
import { DateUtils } from 'ng-jhipster';
import {AbstractService} from "../abstract/abstract.service";

@Injectable()
export class ProjectDocumentationService extends AbstractService<ProjectDocumentation> {

    protected resourceUrl = 'api/project-documentations';
    private findByLabel = 'api/project-documentations/byLabel';

    constructor(protected http: Http, protected dateUtils: DateUtils) {super(http,dateUtils); }

    queryByLabel(label: string): Observable<ProjectDocumentation> {
        return this.http.get(`${this.findByLabel}/${label}`).map((res: Response) => {
            return res.json();
        });
    }

}
