import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { MyJiRSharedModule } from '../../shared';

import {
    SprintService,
} from './';


@NgModule({
    imports: [
        MyJiRSharedModule,
    ],
      providers: [
        SprintService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyJiRSprintModule {}
