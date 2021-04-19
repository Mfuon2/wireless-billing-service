import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {BaseLayoutComponent} from './Layout/base-layout/base-layout.component';
import {PagesLayoutComponent} from './Layout/pages-layout/pages-layout.component';

// DEMO PAGES
// Dashboards
import {AnalyticsComponent} from './DemoPages/Dashboards/analytics/analytics.component';

// Pages
import {ForgotPasswordBoxedComponent} from './DemoPages/UserPages/forgot-password-boxed/forgot-password-boxed.component';
import {LoginBoxedComponent} from './DemoPages/UserPages/login-boxed/login-boxed.component';
import {RegisterBoxedComponent} from './DemoPages/UserPages/register-boxed/register-boxed.component';

// Components
import {AccordionsComponent} from './DemoPages/Components/accordions/accordions.component';
// import {TabsComponent} from './DemoPages/Components/tabs/tabs.component';
import {CarouselComponent} from './DemoPages/Components/carousel/carousel.component';
import {ModalsComponent} from './DemoPages/Components/modals/modals.component';
import {ProgressBarComponent} from './DemoPages/Components/progress-bar/progress-bar.component';
import {PaginationComponent} from './DemoPages/Components/pagination/pagination.component';
import {TooltipsPopoversComponent} from './DemoPages/Components/tooltips-popovers/tooltips-popovers.component';

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

      // Components

      // {path: 'components/tabs', component: TabsComponent, data: {extraParameter: 'componentsMenu'}},
      {path: 'components/accordions', component: AccordionsComponent, data: {extraParameter: 'componentsMenu'}},
      {path: 'components/modals', component: ModalsComponent, data: {extraParameter: 'componentsMenu'}},
      {path: 'components/progress-bar', component: ProgressBarComponent, data: {extraParameter: 'componentsMenu'}},
      {path: 'components/tooltips-popovers', component: TooltipsPopoversComponent, data: {extraParameter: 'componentsMenu'}},
      {path: 'components/carousel', component: CarouselComponent, data: {extraParameter: 'componentsMenu'}},
      {path: 'components/pagination', component: PaginationComponent, data: {extraParameter: 'componentsMenu'}},

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
