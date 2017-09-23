import { Route } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import {BacklogComponent} from "./backlog.component";

export const backlogRoute: Route = {
  path: 'backlog',
  component: BacklogComponent,
  data: {
    authorities: ['ROLE_USER'],
    pageTitle: 'Backlog'
  },
  canActivate: [UserRouteAccessService]
};
