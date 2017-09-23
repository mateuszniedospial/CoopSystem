import {Routes, RouterModule} from "@angular/router";
import {UserRouteAccessService} from "../shared/auth/user-route-access-service";
import {SearchComponent} from "./search.component";
import {ModuleWithProviders} from "@angular/core";
/**
 * Created by Master on 21.05.2017.
 */
const SEARCH_ROUTES: Routes = [{
    path: '',
    component: SearchComponent,
    data: {
        authorities: [],
        pageTitle: 'Search!'
    },
    canActivate: [UserRouteAccessService]
}];

export const routing: ModuleWithProviders = RouterModule.forChild(SEARCH_ROUTES);
