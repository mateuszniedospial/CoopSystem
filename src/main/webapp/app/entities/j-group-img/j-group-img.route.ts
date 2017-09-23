import { Routes,  } from '@angular/router';
import { JGroupImgPopupComponent } from './j-group-img-dialog.component';

export const jGroupImgPopupRoute: Routes = [
  {
    path: 'j-group-img-new',
    component: JGroupImgPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'JGroupImgs'
    },
    outlet: 'popup'
  },
  {
    path: 'j-group-img/:id/edit',
    component: JGroupImgPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'JGroupImgs'
    },
    outlet: 'popup'
  }
];
