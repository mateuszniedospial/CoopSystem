import { Route } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import {SprintProfileComponent} from "./profile.component";

export const sprintProfileRoute: Route = {
  path: 'sprint-profile/:id',
  component: SprintProfileComponent,
  data: {
    authorities: ['ROLE_USER'],
    pageTitle: 'Agile profile'
  },
  canActivate: [UserRouteAccessService]
};
