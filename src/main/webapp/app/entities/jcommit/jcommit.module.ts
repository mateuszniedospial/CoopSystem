import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MyJiRSharedModule } from '../../shared';

import {
    JcommitService,
} from './';



@NgModule({
    imports: [
        MyJiRSharedModule,
    ],
    providers: [
        JcommitService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyJiRJcommitModule {}
