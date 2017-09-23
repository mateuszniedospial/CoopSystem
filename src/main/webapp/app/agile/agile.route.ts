import {Routes, RouterModule} from '@angular/router';

import {backlogRoute} from "./backlog/backlog.route";
import {springRoute} from "./sprint/sprint.route";
import {reportRoute} from "./report/report.route";
import {sprintProfileRoute} from "./profile/profile.route";
import {SprintProfileComponent} from "./profile/profile.component";
import {UserRouteAccessService} from "../shared/auth/user-route-access-service";
import {ModuleWithProviders} from "@angular/core";



let AGILE_ROUTES = [
    backlogRoute,
    springRoute,
    reportRoute,
    sprintProfileRoute
    ];

 const routes:Routes = [
    ...AGILE_ROUTES
];

export const routing: ModuleWithProviders = RouterModule.forChild(routes);

