import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MyJiRSharedModule } from '../../shared';

import {
    AttachmentService,
} from './';



@NgModule({
    imports: [
        MyJiRSharedModule,
    ],
    providers: [
        AttachmentService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyJiRAttachmentModule {}
