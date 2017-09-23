import {Route} from '@angular/router';

import {NavbarComponent} from './layouts';

export const routes: Route[] = [{
    path: '',
    component: NavbarComponent,
    outlet: 'navbar'
},
    {
        path: 'admin',
        loadChildren: './admin/admin.module#MyJiRAdminModule',
        pathMatch: 'prefix'
    },
    {
        path: 'search',
        loadChildren: './search/search.module#MyJiRSearchModule',
        pathMatch: 'prefix'
    },
    {
        path: 'agile',
        loadChildren: './agile/agile.module#MyJiRAgileModule',
        pathMatch: 'prefix'
    },
    {
        path: 'home',
        loadChildren: './home/home.module#MyJiRHomeModule',
        pathMatch: 'prefix'
    },

];
