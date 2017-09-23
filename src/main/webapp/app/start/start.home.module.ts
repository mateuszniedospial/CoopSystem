import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MyJiRSharedModule } from '../shared';

import {SharedModule} from 'primeng/primeng';
import {HOME_ROUTE} from "./start.home.route";
import {StartHomeComponent} from "./start.home.component";

@NgModule({
    imports: [
        MyJiRSharedModule,
        SharedModule,
        RouterModule.forRoot([ HOME_ROUTE ], { useHash: true })
    ],
    declarations: [
        StartHomeComponent,
    ],
    entryComponents: [
    ],
    providers: [
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyJiRStartHomeModule {}
