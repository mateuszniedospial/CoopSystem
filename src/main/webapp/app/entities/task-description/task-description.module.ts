import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { MyJiRSharedModule } from '../../shared';
import {TaskDescriptionService,} from './';


@NgModule({
    imports: [
        MyJiRSharedModule,
    ],

    providers: [
        TaskDescriptionService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyJiRTaskDescriptionModule {}
