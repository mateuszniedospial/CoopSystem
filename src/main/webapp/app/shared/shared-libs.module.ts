import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgJhipsterModule } from 'ng-jhipster';
import { InfiniteScrollModule } from 'angular2-infinite-scroll';
import {AutoCompleteModule} from "primeng/components/autocomplete/autocomplete";
import {ButtonModule} from "primeng/components/button/button";

@NgModule({
    imports: [
        NgbModule.forRoot(),
        NgJhipsterModule.forRoot({
        }),
        InfiniteScrollModule,
        AutoCompleteModule,
        ButtonModule
    ],
    exports: [
        FormsModule,
        HttpModule,
        CommonModule,
        NgbModule,
        NgJhipsterModule,
        InfiniteScrollModule,
        AutoCompleteModule,
        ButtonModule
    ]
})
export class MyJiRSharedLibsModule {}
