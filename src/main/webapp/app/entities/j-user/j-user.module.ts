import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import {SharedModule} from "primeng/components/common/shared";

import { MyJiRSharedModule } from '../../shared';
import {JUserProfileComponent} from './j-user-profile.component';
import {PanelModule} from 'primeng/primeng';
import {AccordionModule} from 'primeng/primeng';
import {PaginatorModule} from 'primeng/primeng';
import {GrowlModule} from 'primeng/primeng';

import {
    JUserService,
    JUserDetailComponent,
    jUserRoute,
} from './';

let ENTITY_STATES = [
    ...jUserRoute,
];

@NgModule({
    imports: [
        MyJiRSharedModule,
        PanelModule,
        SharedModule,
        AccordionModule,
        PaginatorModule,
        GrowlModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        JUserProfileComponent,
        JUserDetailComponent,

    ],
    entryComponents: [
        JUserProfileComponent,
    ],
    providers: [
        JUserService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyJiRJUserModule {}
