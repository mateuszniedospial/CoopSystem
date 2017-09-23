import {  Routes } from '@angular/router';

import { ProjectDocumentationPopupComponent } from './project-documentation-dialog.component';
import { ProjectDocumentationDeletePopupComponent } from './project-documentation-delete-dialog.component';



export const projectDocumentationPopupRoute: Routes = [
  {
    path: 'project-documentation-new',
    component: ProjectDocumentationPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'ProjectDocumentations'
    },
    outlet: 'popup'
  },
  {
    path: 'project-documentation/:id/edit',
    component: ProjectDocumentationPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'ProjectDocumentations'
    },
    outlet: 'popup'
  },
  {
    path: 'project-documentation/:id/delete',
    component: ProjectDocumentationDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'ProjectDocumentations'
    },
    outlet: 'popup'
  }
];
