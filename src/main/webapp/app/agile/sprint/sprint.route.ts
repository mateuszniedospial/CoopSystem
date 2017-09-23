import { Route } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import {SprintComponent} from "./sprint.component";

export const springRoute: Route = {
  path: 'sprintView',
  component: SprintComponent,
  data: {
    authorities: ['ROLE_USER'],
    pageTitle: 'Sprint'
  },
  canActivate: [UserRouteAccessService]
};
