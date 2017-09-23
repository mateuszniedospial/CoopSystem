import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { Ng2Webstorage } from 'ng2-webstorage';

import { MyJiRSharedModule, UserRouteAccessService } from './shared';
import { MyJiRAccountModule } from './account/account.module';
import { MyJiREntityModule } from './entities/entity.module';

import { LayoutRoutingModule } from './layouts';
import { customHttpProvider } from './blocks/interceptor/http.provider';
import { PaginationConfig } from './blocks/config/uib-pagination.config';

import {
JhiMainComponent,
NavbarComponent,
FooterComponent,
ProfileService,
PageRibbonComponent,
ErrorComponent
} from './layouts';
import {FaqComponent} from "./layouts/faq/faq.component";
import {MyJiRStartHomeModule} from "./start/start.home.module";


@NgModule({
    imports: [
        BrowserModule,
        LayoutRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-'}),
        MyJiRSharedModule,
        MyJiRStartHomeModule,
        MyJiRAccountModule,
        MyJiREntityModule
    ],
    declarations: [
        JhiMainComponent,
        NavbarComponent,
        ErrorComponent,
        FaqComponent,
        PageRibbonComponent,
        FooterComponent
    ],
    providers: [
        ProfileService,
        { provide: Window, useValue: window },
        { provide: Document, useValue: document },
        customHttpProvider(),
        PaginationConfig,
        UserRouteAccessService
    ],
    bootstrap: [ JhiMainComponent ]
})
export class MyJiRAppModule {}
