import {  Routes } from '@angular/router';


import { JUserDetailComponent } from './j-user-detail.component';

export const jUserRoute: Routes = [
   {
    path: 'j-user/:id',
    component: JUserDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'JUsers'
    }
  }
];

