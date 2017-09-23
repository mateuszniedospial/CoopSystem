import { RouterModule, Routes} from '@angular/router';

import { UserRouteAccessService } from '../shared';
import { HomeComponent } from './';
import {ModuleWithProviders} from "@angular/core";

export const HOME_ROUTE: Routes = [{
  path: '',
  component: HomeComponent,
  data: {
    authorities: [],
    pageTitle: 'Welcome, Java Hipster!'
  },
  canActivate: [UserRouteAccessService]
}];
export const routing: ModuleWithProviders = RouterModule.forChild(HOME_ROUTE);
