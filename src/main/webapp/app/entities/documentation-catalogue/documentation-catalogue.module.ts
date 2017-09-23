import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MyJiRSharedModule } from '../../shared';

import {
    DocumentationCatalogueService,
    DocumentationCataloguePopupService,
    DocumentationCatalogueDialogComponent,
    DocumentationCataloguePopupComponent,
    DocumentationCatalogueDeletePopupComponent,
    DocumentationCatalogueDeleteDialogComponent,
    documentationCataloguePopupRoute,
    DocumentationCatalogueResolvePagingParams,
} from './';

let ENTITY_STATES = [
    ...documentationCataloguePopupRoute,
];

@NgModule({
    imports: [
        MyJiRSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        DocumentationCatalogueDialogComponent,
        DocumentationCatalogueDeleteDialogComponent,
        DocumentationCataloguePopupComponent,
        DocumentationCatalogueDeletePopupComponent,
    ],
    entryComponents: [
        DocumentationCatalogueDialogComponent,
        DocumentationCataloguePopupComponent,
        DocumentationCatalogueDeleteDialogComponent,
        DocumentationCatalogueDeletePopupComponent,
    ],
    providers: [
        DocumentationCatalogueService,
        DocumentationCataloguePopupService,
        DocumentationCatalogueResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyJiRDocumentationCatalogueModule {}
