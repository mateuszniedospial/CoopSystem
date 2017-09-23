import { Routes,  } from '@angular/router';
import { JGroupDetailComponent } from './j-group-detail.component';
import { JGroupPopupComponent } from './j-group-dialog.component';
import {JGroupRequestsComponent} from "./j-group-requests.component";
import {JGroupLeavePopupComponent} from "./j-group-leave-dialog.component";


export const jGroupRoute: Routes = [
  {
    path: 'j-group/:id',
    component: JGroupDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'JGroups'
    }
  }, {
        path: 'j-group/:id/requests',
        component: JGroupRequestsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'JGroups'
    }
  }
];

export const jGroupPopupRoute: Routes = [
  {
    path: 'j-group-new',
    component: JGroupPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'JGroups'
    },
    outlet: 'popup'
  },
  {
    path: 'j-group/:id/edit',
    component: JGroupPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'JGroups'
    },
    outlet: 'popup'
  },
   {
    path: 'j-group/leave',
    component: JGroupLeavePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'JGroups'
    },
    outlet: 'popup'
  }
];
