import { Route } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import {ReportComponent} from "./report.component";

export const reportRoute: Route = {
  path: 'reportView/:id',
  component: ReportComponent,
  data: {
    authorities: ['ROLE_USER'],
    pageTitle: 'Report'
  },
  canActivate: [UserRouteAccessService]
};
