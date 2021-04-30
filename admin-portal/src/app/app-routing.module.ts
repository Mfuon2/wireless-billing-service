import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {BaseLayoutComponent} from './Layout/base-layout/base-layout.component';
import {PagesLayoutComponent} from './Layout/pages-layout/pages-layout.component';

// DEMO PAGES
// Dashboards
import {AnalyticsComponent} from './Pages/Dashboards/analytics/analytics.component';

// Pages
import {ForgotPasswordBoxedComponent} from './Pages/UserPages/forgot-password-boxed/forgot-password-boxed.component';
import {LoginBoxedComponent} from './Pages/UserPages/login-boxed/login-boxed.component';
import {RegisterBoxedComponent} from './Pages/UserPages/register-boxed/register-boxed.component';

// Components
import {ModalsComponent} from './Pages/Components/modals/modals.component';
import {ProgressBarComponent} from './Pages/Components/progress-bar/progress-bar.component';
import {ClientsComponent} from './Pages/clients/clients.component';
import {ServicePackagesComponent} from './Pages/service-packages/service-packages.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },
  {path: 'login', component: LoginBoxedComponent},
  {path: 'reset-password', component: ForgotPasswordBoxedComponent},
  {
    path: 'layout',
    component: BaseLayoutComponent,
    children: [

      // Dashboads

      {path: '', component: AnalyticsComponent, data: {extraParameter: 'dashboardsMenu'}},

      // Clients
      {path: 'clients/account', component: ClientsComponent, data: {extraParameter: 'clientMenu'}},
      {path: 'service/packages', component: ServicePackagesComponent, data: {extraParameter: 'serviceMenu'}},

      {path: 'components/modals', component: ModalsComponent, data: {extraParameter: 'componentsMenu'}},
      {path: 'components/progress-bar', component: ProgressBarComponent, data: {extraParameter: 'componentsMenu'}},

    ]

  },
  {
    path: '',
    component: PagesLayoutComponent,
    children: [

      // User Pages

      {path: 'pages/login-boxed', component: LoginBoxedComponent, data: {extraParameter: ''}},
      {path: 'pages/register-boxed', component: RegisterBoxedComponent, data: {extraParameter: ''}},
      {path: 'pages/forgot-password-boxed', component: ForgotPasswordBoxedComponent, data: {extraParameter: ''}},
    ]
  },
  {path: '**', redirectTo: ''}
];

@NgModule({
  imports: [RouterModule.forRoot(routes,
      {
        scrollPositionRestoration: 'enabled',
        anchorScrolling: 'enabled',
        relativeLinkResolution: 'legacy'
      })],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
