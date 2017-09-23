import {  Routes,  } from '@angular/router';
import {WorkLogOfficialPopupComponent} from "./work-log-official-dialog.component";



export const workLogPopupRoute: Routes = [
    {
        path: 'work-log-official-new',
        component: WorkLogOfficialPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WorkLogs'
        },
        outlet: 'popup'
    }
];
