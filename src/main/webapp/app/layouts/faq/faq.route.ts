import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import {FaqComponent} from "./faq.component";

export const faqRoute: Routes = [
  {
    path: 'faq',
    component: FaqComponent,
    data: {
      authorities: [],
      pageTitle: 'FAQ!'
    },
    // canActivate: [UserRouteAccessService]
  },
];
