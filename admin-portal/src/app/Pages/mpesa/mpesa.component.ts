import {Component, OnInit} from '@angular/core';
import {MpesaService} from './mpesa.service';
import {Content, StkPushRequestModel} from '../../Models/Mpesa/express-mpesa';
import {Utils} from '../../Common/utils/utils';

@Component({
  selector: 'app-mpesa',
  templateUrl: './mpesa.component.html'
})
export class MpesaComponent implements OnInit {

  heading = 'M-Pesa Stk';
  subheading = 'Lists all STK Transactions';
  icon = 'pe-7s-gleam icon-gradient bg-tempting-azure';

  datasource: Content[];
  totalRecords: number;
  loading: boolean;
  clientData: StkPushRequestModel;
  submitted: boolean;
  clientDialog: boolean;
  errorOnCreation: boolean;
  errorMessage: any;
  subscriptionPlan: any[];
  serviceType: any[];
  paymentTypes: any[];

  constructor(
      private mps: MpesaService,
      private utils: Utils
  ) {
  }

  ngOnInit(): void {
    this.loadTransactions();
    this.loadStaticDropDowns();
  }

  loadTransactions() {
    return this.mps.GetPagedExpressTransactions()
        .subscribe((data) => {
          this.datasource = data.content;
          this.totalRecords = data.totalElements;
          this.loading = false;
        });
  }

  openNew() {
    this.clientData = ({} as any) as StkPushRequestModel;
    this.submitted = false;
    this.clientDialog = true;
  }

  hideDialog() {
    this.clientDialog = false;
    this.submitted = false;
  }

  requestStkPush() {
    this.submitted = true;
    return this.mps.SendStkPush(JSON.stringify(this.clientData))
        .subscribe((clientResponse) => {
          if (clientResponse.success) {
            this.utils.showSuccess(clientResponse.msg);
            this.hideDialog();
            this.checkTransactionStatus(clientResponse.data.checkoutRequestId)
                .then(r => this.utils.showSuccess('Successfully Loaded transactionStatus'));
          } else {
            this.errorOnCreation = true;
            this.errorMessage = clientResponse.msg;
            this.utils.showError(clientResponse.msg);
          }
        }, (error) => {
          this.errorOnCreation = true;
          this.errorMessage = error;
          this.utils.showError(error);
        }, () => {
          this.reload();
        });
  }

  private async checkTransactionStatus(checkoutRequestId: any) {
    const resp = await this.mps.GetTransactionStatus(checkoutRequestId)
        .then(
            (data) => {
              data.toPromise()
                  .then((res) => {
                    if (res.success) {
                      this.utils.showSuccess(res.data.resultDescription);
                      this.hideDialog();
                    } else {
                      this.errorOnCreation = true;
                      this.errorMessage = res.msg;
                      this.utils.showError(res.msg);
                    }
                  }).finally(() => {
                this.reload();
              });
            }
        );
  }

  private loadStaticDropDowns() {
    this.subscriptionPlan = [
      {label: 'PERSONAL', value: 'PERSONAL'},
      {label: 'DEMO', value: 'DEMO'},
      {label: 'PLATINUM', value: 'PLATINUM'},
      {label: 'BUSINESS', value: 'BUSINESS'}
    ];

    this.serviceType = [
      {label: 'PRE PAID SERVICE', value: 'PRE_PAID'},
      {label: 'ADHOC SERVICE', value: 'ADHOC'},
      {label: 'POST PAID SERVICE', value: 'POST_PAID'}
    ];

    this.paymentTypes = [
      {label: 'PAYMENT', value: 'PAYMENT'},
      {label: 'DEPOSIT', value: 'DEPOSIT'}
    ];
  }

  private reload() {
    this.ngOnInit();
  }
}
