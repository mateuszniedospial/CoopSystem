import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MyJiRSharedModule } from '../../shared';

import {
    WorkLogService,
    WorkLogPopupService,
    workLogPopupRoute,
} from './';
import {WorkLogOfficialPopupComponent, WorkLogOfficialDialogComponent} from "./work-log-official-dialog.component";

let ENTITY_STATES = [
    ...workLogPopupRoute,
];

@NgModule({
    imports: [
        MyJiRSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        WorkLogOfficialPopupComponent,
        WorkLogOfficialDialogComponent
    ],
    entryComponents: [
        WorkLogOfficialPopupComponent,
        WorkLogOfficialDialogComponent
    ],
    providers: [
        WorkLogService,
        WorkLogPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyJiRWorkLogModule {}
