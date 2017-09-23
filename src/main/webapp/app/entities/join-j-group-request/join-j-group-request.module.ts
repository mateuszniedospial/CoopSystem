import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MyJiRSharedModule } from '../../shared';

import {
    JoinJGroupRequestService,
    JoinJGroupRequestPopupService,
    JoinJGroupRequestDialogComponent,
    JoinJGroupRequestPopupComponent,
    joinJGroupRequestPopupRoute,
} from './';

let ENTITY_STATES = [
    ...joinJGroupRequestPopupRoute,
];

@NgModule({
    imports: [
        MyJiRSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        JoinJGroupRequestDialogComponent,
        JoinJGroupRequestPopupComponent,
    ],
    entryComponents: [
        JoinJGroupRequestDialogComponent,
        JoinJGroupRequestPopupComponent,
    ],
    providers: [
        JoinJGroupRequestService,
        JoinJGroupRequestPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyJiRJoinJGroupRequestModule {}
