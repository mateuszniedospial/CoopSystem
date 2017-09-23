/**
 * Created by Master on 21.05.2017.
 */
import {MyJiRSharedModule} from "../shared/shared.module";
import {NgModule} from "@angular/core";
import {SearchComponent} from "./search.component";
import { routing} from "./search.route";
import {RouterModule} from "@angular/router";
import {SearchService} from "./search.service";
@NgModule({
    imports: [
        MyJiRSharedModule,
        routing
    ],
    declarations: [
        SearchComponent
    ],

    providers: [
        SearchService
    ],
    schemas: []
})
export class MyJiRSearchModule {}
