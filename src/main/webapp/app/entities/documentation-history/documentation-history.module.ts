import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { MyJiRSharedModule } from '../../shared';

import {
    DocumentationHistoryService,
} from './';


@NgModule({
    imports: [
        MyJiRSharedModule,
    ],
    providers: [
        DocumentationHistoryService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyJiRDocumentationHistoryModule {}
