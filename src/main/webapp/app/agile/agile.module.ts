import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MyJiRSharedModule } from '../shared';
import {BacklogComponent} from './backlog/backlog.component';
import {routing} from './agile.route';
import {DragulaModule} from 'ng2-dragula';

import {
} from './';
import {SprintComponent} from './sprint/sprint.component';
import {AccordionModule} from 'primeng/components/accordion/accordion';
import {GrowlModule} from "primeng/components/growl/growl";
import {ReportComponent} from "./report/report.component";
import {ChartsModule} from "ng2-charts";
import {SprintProfileComponent} from "./profile/profile.component";

@NgModule({
    imports: [
        MyJiRSharedModule,
        DragulaModule,
        AccordionModule,
        routing,
        GrowlModule,
        ChartsModule
    ],
    declarations: [
        BacklogComponent,
        SprintComponent,
        ReportComponent,
        SprintProfileComponent
    ],

    providers: [

    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyJiRAgileModule {}
