import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MyJiRSharedModule } from '../../shared';

import {
    PokerService,
    PokerPopupService,
    PokerOfficialPopupComponent,
    PokerOfficialDialogComponent,
    pokerRoute,
    pokerPopupRoute,
} from './';
import {GrowlModule} from "primeng/components/growl/growl";
import {PokerVotesService} from "./poker-votes.service";

let ENTITY_STATES = [
    ...pokerRoute,
    ...pokerPopupRoute,
];

@NgModule({
    imports: [
        MyJiRSharedModule,
        RouterModule.forChild(ENTITY_STATES),
        GrowlModule
    ],
    declarations: [
        PokerOfficialPopupComponent,
        PokerOfficialDialogComponent,
    ],
    entryComponents: [
        PokerOfficialPopupComponent,
        PokerOfficialDialogComponent,
    ],
    providers: [
        PokerService,
        PokerPopupService,
        PokerVotesService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyJiRPokerModule {}
