import {  Routes, } from '@angular/router';
import { JoinJGroupRequestPopupComponent } from './join-j-group-request-dialog.component';

export const joinJGroupRequestPopupRoute: Routes = [
  {
    path: 'join-j-group-request-new',
    component: JoinJGroupRequestPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'JoinJGroupRequests'
    },
    outlet: 'popup'
  },
  {
    path: 'join-j-group-request/:id/edit',
    component: JoinJGroupRequestPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'JoinJGroupRequests'
    },
    outlet: 'popup'
  }
];
