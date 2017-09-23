import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MyJiRSharedModule } from '../../shared';

import {
    TaskService,
    TaskPopupService,
    taskRoute,
    taskPopupRoute,
} from './';
import {TaskOfficialDetailComponent} from "./task-official-detail.component";
import {AccordionModule} from "primeng/components/accordion/accordion";
import {SharedModule} from "primeng/components/common/shared";
import {TabViewModule} from "primeng/components/tabview/tabview";
import {DropdownModule} from "primeng/components/dropdown/dropdown";
import {AutoCompleteModule} from "primeng/components/autocomplete/autocomplete";
import {InputTextModule} from "primeng/components/inputtext/inputtext";
import {FieldsetModule} from "primeng/components/fieldset/fieldset";
import {commentPopupRoute} from "../comment/comment.route";
import {GrowlModule} from "primeng/components/growl/growl";
import {TaskOfficialPopupComponent, TaskOfficialDialogComponent} from "./task-official-dialog.component";

let ENTITY_STATES = [
    ...taskRoute,
    ...taskPopupRoute,
    ...commentPopupRoute
];

@NgModule({
    imports: [
        MyJiRSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true }),
        AccordionModule,
        SharedModule,
        TabViewModule,
        DropdownModule,
        AutoCompleteModule,
        InputTextModule,
        FieldsetModule,
        GrowlModule
    ],
    declarations: [
        TaskOfficialDetailComponent,
        TaskOfficialDialogComponent,
        TaskOfficialPopupComponent
    ],
    entryComponents: [
        TaskOfficialDetailComponent,
        TaskOfficialDialogComponent,
        TaskOfficialPopupComponent
    ],
    providers: [
        TaskService,
        TaskPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyJiRTaskModule {}
