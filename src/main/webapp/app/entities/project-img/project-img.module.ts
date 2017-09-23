import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MyJiRSharedModule } from '../../shared';

import {
    ProjectImgService,
    ProjectImgPopupService,
    ProjectImgDialogComponent,
    ProjectImgPopupComponent,
    projectImgPopupRoute,
} from './';

let ENTITY_STATES = [
    ...projectImgPopupRoute,
];

@NgModule({
    imports: [
        MyJiRSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ProjectImgDialogComponent,
        ProjectImgPopupComponent,
    ],
    entryComponents: [
        ProjectImgDialogComponent,
        ProjectImgPopupComponent,
    ],
    providers: [
        ProjectImgService,
        ProjectImgPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyJiRProjectImgModule {}
