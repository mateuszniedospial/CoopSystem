import { Routes } from '@angular/router';
import { ProjectComponent } from './project.component';
import { ProjectPopupComponent } from './project-dialog.component';

import {ProjectReportComponent } from './project-report.component';

import {ProjectDocumentationComponent} from "./project-documentation.component";
import {ProjectDocumentationRevertPopupComponent} from "./project-documentation-revert-dialog.component";
import {ProjectViewComponent} from "./project-view.component";
import {ProjectsMainComponent} from "./projects-main.component";

export const projectRoute: Routes = [
  {
    path: 'project/all/view',
    component: ProjectComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Projects'
    }
  }, {
    path: 'project',
    component: ProjectsMainComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Projects'
    }
  },{
    path: 'project/:id',
    component: ProjectViewComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Projects'
    }
  },{
    path: 'project/:id/report',
    component: ProjectReportComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Projects'
    }
  }, {
    path: 'project/:id/documentation',
    component: ProjectDocumentationComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'ProjectDocumentation'
    }
  }
];

export const projectPopupRoute: Routes = [
  {
    path: 'project-new',
    component: ProjectPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Projects'
    },
    outlet: 'popup'
  },
  {
    path: 'project/:id/edit',
    component: ProjectPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Projects'
    },
    outlet: 'popup'
  },
  {
    path: 'project/:id/documentation/:id/revert',
    component: ProjectDocumentationRevertPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'ProjectDocumentation'
    },
    outlet: 'popup'
  },
];
