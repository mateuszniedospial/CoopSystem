import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MyJiRSharedModule } from '../../shared';

import {PanelModule} from 'primeng/primeng';
import {TreeModule} from 'primeng/primeng';
import {EditorModule,SharedModule} from 'primeng/primeng';
import {GrowlModule} from 'primeng/primeng';
import {DropdownModule} from 'primeng/primeng';
import {ButtonModule} from 'primeng/primeng';

import {
    ProjectService,
    ProjectPopupService,
    ProjectComponent,
    ProjectDialogComponent,
    ProjectPopupComponent,
    ProjectDocumentationComponent,
    ProjectReportComponent,
    ProjectDocumentationRevertDialogComponent,
    ProjectDocumentationRevertPopupComponent,
    projectRoute,
    projectPopupRoute,
    ProjectViewComponent,
    ProjectsMainComponent
} from './';

let ENTITY_STATES = [
    ...projectRoute,
    ...projectPopupRoute,
];

@NgModule({
    imports: [
        MyJiRSharedModule,
        TreeModule,
        EditorModule,
        GrowlModule,
        DropdownModule,
        ButtonModule,
        SharedModule,
        PanelModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ProjectComponent,
        ProjectDialogComponent,
        ProjectReportComponent,
        ProjectDocumentationComponent,
        ProjectDocumentationRevertDialogComponent,
        ProjectDocumentationRevertPopupComponent,
        ProjectPopupComponent,
        ProjectViewComponent,
        ProjectsMainComponent
    ],
    entryComponents: [
        ProjectComponent,
        ProjectDialogComponent,
        ProjectDocumentationComponent,
        ProjectPopupComponent,
        ProjectDocumentationRevertDialogComponent,
        ProjectDocumentationRevertPopupComponent,
        ProjectReportComponent,
        ProjectViewComponent,
        ProjectsMainComponent
    ],
    providers: [
        ProjectService,
        ProjectPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyJiRProjectModule {}
