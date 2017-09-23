import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import {ScheduleModule} from 'primeng/primeng';

import { MyJiRSharedModule } from '../../shared';

import {
    JGroupImgService,
    JGroupImgPopupService,
    JGroupImgDialogComponent,
    JGroupImgPopupComponent,
    jGroupImgPopupRoute,
} from './';

let ENTITY_STATES = [
    ...jGroupImgPopupRoute,
];

@NgModule({
    imports: [
        MyJiRSharedModule,
        ScheduleModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        JGroupImgDialogComponent,
        JGroupImgPopupComponent,
    ],
    entryComponents: [
        JGroupImgDialogComponent,
        JGroupImgPopupComponent,
    ],
    providers: [
        JGroupImgService,
        JGroupImgPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyJiRJGroupImgModule {}
