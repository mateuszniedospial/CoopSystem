import { NgModule, Sanitizer } from '@angular/core';
import { Title } from '@angular/platform-browser';

import { AlertService } from 'ng-jhipster';

import {NdvEditComponent} from "angular2-click-to-edit/lib/ndv.edit.component";
import {NdvEditAreaComponent} from "angular2-click-to-edit/lib/ndv.edit.area.component";
import {NdvEditSelectComponent} from "angular2-click-to-edit/lib/ndv.edit.select.component"
import {NdvEditSearchComponent} from "angular2-click-to-edit/lib/ndv.edit.search.component"

import {
    MyJiRSharedLibsModule,
    JhiAlertComponent,
    JhiAlertErrorComponent
} from './';
import {JGroupStorageComponent} from "./groupLocalStorage/jgroup-storage.component";

export function alertServiceProvider(sanitizer: Sanitizer) {
    // set below to true to make alerts look like toast
    let isToast = true;
    return new AlertService(sanitizer, isToast);
}

@NgModule({
    imports: [
        MyJiRSharedLibsModule
    ],
    declarations: [
        JhiAlertComponent,
        JhiAlertErrorComponent,
        JGroupStorageComponent,
        NdvEditComponent,
        NdvEditAreaComponent,
        NdvEditSelectComponent,
        NdvEditSearchComponent,
    ],
    providers: [
        {
            provide: AlertService,
            useFactory: alertServiceProvider,
            deps: [Sanitizer]
        },
        Title
    ],
    exports: [
        MyJiRSharedLibsModule,
        JhiAlertComponent,
        JhiAlertErrorComponent,
        NdvEditComponent,
        NdvEditAreaComponent,
        NdvEditSelectComponent,
        NdvEditSearchComponent,
        JGroupStorageComponent,
    ]
})
export class MyJiRSharedCommonModule {}
