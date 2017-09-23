import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { PaginationUtil } from 'ng-jhipster';

import { DocumentationCataloguePopupComponent } from './documentation-catalogue-dialog.component';
import { DocumentationCatalogueDeletePopupComponent } from './documentation-catalogue-delete-dialog.component';


@Injectable()
export class DocumentationCatalogueResolvePagingParams implements Resolve<any> {

  constructor(private paginationUtil: PaginationUtil) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
      let page = route.queryParams['page'] ? route.queryParams['page'] : '1';
      let sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
      return {
          page: this.paginationUtil.parsePage(page),
          predicate: this.paginationUtil.parsePredicate(sort),
          ascending: this.paginationUtil.parseAscending(sort)
    };
  }
}


export const documentationCataloguePopupRoute: Routes = [
  {
    path: 'documentation-catalogue-new',
    component: DocumentationCataloguePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'DocumentationCatalogues'
    },
    outlet: 'popup'
  },
  {
    path: 'documentation-catalogue/:id/edit',
    component: DocumentationCataloguePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'DocumentationCatalogues'
    },
    outlet: 'popup'
  },
  {
    path: 'documentation-catalogue/:id/delete',
    component: DocumentationCatalogueDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'DocumentationCatalogues'
    },
    outlet: 'popup'
  }
];
