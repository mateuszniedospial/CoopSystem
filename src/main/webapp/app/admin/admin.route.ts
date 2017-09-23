import {Routes, CanActivate, RouterModule} from '@angular/router';

import {
    auditsRoute,
    configurationRoute,
    docsRoute,
    healthRoute,
    logsRoute,
    metricsRoute,
    userMgmtRoute,
    userDialogRoute
} from './';

import { UserRouteAccessService } from '../shared';
import {ModuleWithProviders} from "@angular/core";
import {UserMgmtComponent} from "./user-management/user-management.component";
import {UserResolvePagingParams} from "./user-management/user-management.route";

let ADMIN_ROUTES = [
    auditsRoute,
    configurationRoute,
    docsRoute,
    healthRoute,
    logsRoute,
    ...userMgmtRoute,
    metricsRoute
];


export const routes:Routes = [
    {
        path: '',
        component: UserMgmtComponent,
        data: {
            authorities: ['ROLE_ADMIN']
        },
        resolve: {
            'pagingParams': UserResolvePagingParams
        },
        canActivate: [UserRouteAccessService],
    },
    ...ADMIN_ROUTES,
    ...userDialogRoute
];

export const routing: ModuleWithProviders = RouterModule.forChild(routes);

