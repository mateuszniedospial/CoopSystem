import { Routes,  } from '@angular/router';



import {CommentOfficialPopupComponent} from "./comment-official-dialog.component";



export const commentPopupRoute: Routes = [
    {
        path: 'comment-official-new',
        component: CommentOfficialPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Comments'
        },
        outlet: 'popup'
    }
];
