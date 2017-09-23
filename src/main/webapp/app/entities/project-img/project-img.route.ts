import {  Routes } from '@angular/router';
import {ProjectImgPopupComponent} from "./project-img-dialog.component";

export const projectImgPopupRoute: Routes = [
  {
    path: 'project-img-new',
    component: ProjectImgPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'ProjectImgs'
    },
    outlet: 'popup'
  },
  {
    path: 'project-img/:id/edit',
    component: ProjectImgPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'ProjectImgs'
    },
    outlet: 'popup'
  },
];
