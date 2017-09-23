import { Route } from '@angular/router';

import { UserRouteAccessService } from '../shared';
import {StartHomeComponent} from "./start.home.component";

export const HOME_ROUTE: Route = {
  path: '',
  component: StartHomeComponent,
  data: {
    authorities: [],
    pageTitle: 'Welcome, Java Hipster!'
  },
  canActivate: [UserRouteAccessService]
};
