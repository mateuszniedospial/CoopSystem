import { Routes } from '@angular/router';
import {TaskOfficialDetailComponent} from "./task-official-detail.component";
import {TaskOfficialPopupComponent} from "./task-official-dialog.component";


export const taskRoute: Routes = [
    {
        path: 'task/view/:id',
        component: TaskOfficialDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Tasks'
        }
    }
];

export const taskPopupRoute = [
     {
        path: 'task-official-new',
        component: TaskOfficialPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Tasks'
        },
        outlet: 'popup'
    }
];
