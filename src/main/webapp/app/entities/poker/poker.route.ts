import {  Routes } from '@angular/router';



import {PokerOfficialPopupComponent} from "./poker-official-dialog.component";


export const pokerRoute: Routes = [
];

export const pokerPopupRoute: Routes = [
    {
        path: 'poker-official-new',
        component: PokerOfficialPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Pokers'
        },
        outlet: 'popup'
    },

];
