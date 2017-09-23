import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MyJiRSharedModule } from '../../shared';

import {
    CommentService,
    CommentPopupService,
    commentPopupRoute,
} from './';
import {CommentOfficialPopupComponent, CommentOfficialComponent} from "./comment-official-dialog.component";

let ENTITY_STATES = [
    ...commentPopupRoute,
];

@NgModule({
    imports: [
        MyJiRSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CommentOfficialPopupComponent,
        CommentOfficialComponent
    ],
    entryComponents: [
        CommentOfficialPopupComponent,
        CommentOfficialComponent
    ],
    providers: [
        CommentService,
        CommentPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyJiRCommentModule {}

