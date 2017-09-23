import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { MyJiRSharedModule } from '../../shared';
import {
    ReportService,
} from './';

@NgModule({
    imports: [
        MyJiRSharedModule,
    ],
    providers: [
        ReportService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyJiRReportModule {}
