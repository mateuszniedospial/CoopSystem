import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { MyJiRSharedModule } from '../../shared';

import {TaskHistoryService,} from './';

@NgModule({
    imports: [
        MyJiRSharedModule,
    ],
    providers: [
        TaskHistoryService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyJiRTaskHistoryModule {}
