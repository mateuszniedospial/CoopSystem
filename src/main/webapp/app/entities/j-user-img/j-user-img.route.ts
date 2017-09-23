import {  Routes,  } from '@angular/router';
import { JUserImgPopupComponent } from './j-user-img-dialog.component';

export const jUserImgPopupRoute: Routes = [
  {
    path: 'j-user-img-new',
    component: JUserImgPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'JUserImgs'
    },
    outlet: 'popup'
  },
  {
    path: 'j-user-img/:id/edit',
    component: JUserImgPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'JUserImgs'
    },
    outlet: 'popup'
  },
];
