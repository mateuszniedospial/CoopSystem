import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MyJiRSharedModule } from '../../shared';
import {JGroupProfileComponent} from "./j-group-profile.component";
import {PanelModule} from 'primeng/primeng';
import {AccordionModule} from 'primeng/primeng';
import {GrowlModule} from 'primeng/primeng';
import {ButtonModule} from 'primeng/primeng';

import {
    JGroupService,
    JGroupPopupService,
    JGroupDetailComponent,
    JGroupDialogComponent,
    JGroupPopupComponent,
    JGroupLeaveDialogComponent,
    JGroupLeavePopupComponent,
    JGroupRequestsComponent,
    jGroupRoute,
    jGroupPopupRoute,
} from './';


let ENTITY_STATES = [
    ...jGroupRoute,
    ...jGroupPopupRoute,
];

@NgModule({
    imports: [
        MyJiRSharedModule,
        PanelModule,
        AccordionModule,
        GrowlModule,
        ButtonModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        JGroupProfileComponent,
        JGroupDetailComponent,
        JGroupDialogComponent,
        JGroupPopupComponent,
        JGroupLeaveDialogComponent,
        JGroupLeavePopupComponent,
        JGroupRequestsComponent
    ],
    entryComponents: [
        JGroupProfileComponent,
        JGroupDialogComponent,
        JGroupPopupComponent,
        JGroupLeaveDialogComponent,
        JGroupLeavePopupComponent,
        JGroupRequestsComponent
    ],
    providers: [
        JGroupService,
        JGroupPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyJiRJGroupModule {}
