import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { MyJiRProjectDocumentationModule } from './project-documentation/project-documentation.module';
import { MyJiRDocumentationHistoryModule } from './documentation-history/documentation-history.module';
import { MyJiRJGroupModule } from './j-group/j-group.module';
import { MyJiRJGroupImgModule } from './j-group-img/j-group-img.module';
import { MyJiRJUserModule } from './j-user/j-user.module';
import { MyJiRJUserImgModule } from './j-user-img/j-user-img.module';
import { MyJiRProjectModule } from './project/project.module';
import { MyJiRCommentModule } from './comment/comment.module';
import { MyJiRProjectImgModule } from './project-img/project-img.module';
import { MyJiRReportModule } from './report/report.module';
import { MyJiRSprintModule } from './sprint/sprint.module';
import { MyJiRTaskModule } from './task/task.module';
import { MyJiRTaskDescriptionModule } from './task-description/task-description.module';
import { MyJiRAttachmentModule } from './attachment/attachment.module';
import { MyJiRWorkLogModule } from './work-log/work-log.module';
import { MyJiRTaskHistoryModule } from './task-history/task-history.module';
import { MyJiRPokerModule } from './poker/poker.module';
import { MyJiRJcommitModule } from './jcommit/jcommit.module';
import { MyJiRDocumentationCatalogueModule } from './documentation-catalogue/documentation-catalogue.module';
import { MyJiRJoinJGroupRequestModule } from './join-j-group-request/join-j-group-request.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        MyJiRProjectDocumentationModule,
        MyJiRDocumentationHistoryModule,
        MyJiRJGroupModule,
        MyJiRJGroupImgModule,
        MyJiRJUserModule,
        MyJiRJUserImgModule,
        MyJiRProjectModule,
        MyJiRCommentModule,
        MyJiRProjectImgModule,
        MyJiRReportModule,
        MyJiRSprintModule,
        MyJiRTaskModule,
        MyJiRTaskDescriptionModule,
        MyJiRAttachmentModule,
        MyJiRWorkLogModule,
        MyJiRTaskHistoryModule,
        MyJiRPokerModule,
        MyJiRJcommitModule,
        MyJiRPokerModule,
        MyJiRDocumentationCatalogueModule,
        MyJiRJoinJGroupRequestModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyJiREntityModule {}
