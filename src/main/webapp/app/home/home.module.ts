import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MyJiRSharedModule } from '../shared';

import { routing, HomeComponent} from './';
import {GroupsUserDataComponent} from "./groups-user-data.component";
import {TabViewModule} from 'primeng/primeng';
import {FieldsetModule} from 'primeng/primeng';
import {AccordionModule} from 'primeng/primeng';
import {DataTableModule,SharedModule} from 'primeng/primeng';
import {PanelModule} from 'primeng/primeng';

@NgModule({
    imports: [
        MyJiRSharedModule,
        TabViewModule,
        FieldsetModule,
        AccordionModule,
        DataTableModule,
        SharedModule,
        PanelModule,
        routing
    ],
    declarations: [
        HomeComponent,
        GroupsUserDataComponent
    ],
    entryComponents: [
    ],
    providers: [
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyJiRHomeModule {}
