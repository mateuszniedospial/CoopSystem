import { NgModule } from '@angular/core';
import { RouterModule, Routes, Resolve } from '@angular/router';

import { routes } from '../app.route';
import { errorRoute } from './';
import {faqRoute} from "./faq/faq.route";

let LAYOUT_ROUTES = [
        ...routes,
    ...errorRoute,
    ...faqRoute
];

@NgModule({
  imports: [
    RouterModule.forRoot(LAYOUT_ROUTES, { useHash: true })
  ],
  exports: [
    RouterModule
  ]
})
export class LayoutRoutingModule {}
