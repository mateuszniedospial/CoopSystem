import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MyJiRSharedModule } from '../../shared';

import {
    ProjectDocumentationService,
    ProjectDocumentationPopupService,
    ProjectDocumentationDialogComponent,
    ProjectDocumentationPopupComponent,
    ProjectDocumentationDeletePopupComponent,
    ProjectDocumentationDeleteDialogComponent,
    projectDocumentationPopupRoute,
} from './';

let ENTITY_STATES = [
    ...projectDocumentationPopupRoute,
];

@NgModule({
    imports: [
        MyJiRSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ProjectDocumentationDialogComponent,
        ProjectDocumentationDeleteDialogComponent,
        ProjectDocumentationPopupComponent,
        ProjectDocumentationDeletePopupComponent,
    ],
    entryComponents: [
        ProjectDocumentationDialogComponent,
        ProjectDocumentationPopupComponent,
        ProjectDocumentationDeleteDialogComponent,
        ProjectDocumentationDeletePopupComponent,
    ],
    providers: [
        ProjectDocumentationService,
        ProjectDocumentationPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyJiRProjectDocumentationModule {}
