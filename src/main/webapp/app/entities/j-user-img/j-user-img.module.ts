import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MyJiRSharedModule } from '../../shared';

import {
    JUserImgService,
    JUserImgPopupService,
    JUserImgDialogComponent,
    JUserImgPopupComponent,
    jUserImgPopupRoute,
} from './';

let ENTITY_STATES = [
    ...jUserImgPopupRoute,
];

@NgModule({
    imports: [
        MyJiRSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        JUserImgDialogComponent,
        JUserImgPopupComponent,
    ],
    entryComponents: [
        JUserImgDialogComponent,
        JUserImgPopupComponent,
    ],
    providers: [
        JUserImgService,
        JUserImgPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyJiRJUserImgModule {}
