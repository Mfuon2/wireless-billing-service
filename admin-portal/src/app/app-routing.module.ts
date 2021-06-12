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
import {ClientsComponent} from './Pages/clients/clients.component';
import {ServicePackagesComponent} from './Pages/service-packages/service-packages.component';
import {SubscriptionComponent} from './Pages/subscription/subscription.component';
import {WalletComponent} from './Pages/wallet/wallet.component';
import {MpesaComponent} from './Pages/mpesa/mpesa.component';
import {NotfoundComponent} from './Pages/UserPages/notfound/notfound.component';
import {PaybillComponent} from './Pages/paybill/confirmation/paybill.component';
import {ValidationComponent} from './Pages/paybill/validation/validation.component';
import {EquityComponent} from './Pages/equity/equity.component';
import {VoucherComponent} from './Pages/voucher/voucher.component';
import {SmsComponent} from './Pages/sms/sms.component';
import {AuthGuard} from './auth/auth.guard';

const routes: Routes = [
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },
  {path: 'login', component: LoginBoxedComponent},
  {path: 'reset-password', component: ForgotPasswordBoxedComponent},
  {
    path: 'en',
    component: BaseLayoutComponent,
    canActivate: [AuthGuard],
    children: [

      // Dashboads

      {path: '', component: AnalyticsComponent, data: {extraParameter: 'dashboardsMenu'}},

      // Clients
      {path: 'clients/account', component: ClientsComponent, data: {extraParameter: 'clientMenu'}},
      {path: 'service/packages', component: ServicePackagesComponent, data: {extraParameter: 'serviceMenu'}},
      {path: 'clients/subscriptions', component: SubscriptionComponent, data: {extraParameter: 'subscriptionsMenu'}},
      {path: 'payment/wallet', component: WalletComponent, data: {extraParameter: 'walletMenu'}},
      {path: 'pay/express', component: MpesaComponent, data: {extraParameter: 'expressMenu'}},
      {path: 'transaction/paybill/confirmation', component: PaybillComponent, data: {extraParameter: 'payBillMenu'}},
      {path: 'transaction/paybill/validation', component: ValidationComponent, data: {extraParameter: 'payBillMenu'}},
      {path: 'transaction/equity', component: EquityComponent, data: {extraParameter: 'equityMenu'}},
      {path: 'payment/voucher', component: VoucherComponent, data: {extraParameter: 'voucherMenu'}},
      {path: 'transaction/sms', component: SmsComponent, data: {extraParameter: 'smsMenu'}}
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
  { path: '404', component: NotfoundComponent},
  { path: '**', redirectTo: '404' }
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
