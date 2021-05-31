import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {MpesaService} from './mpesa.service';
import {Content, StkPushRequestModel} from '../../Models/Mpesa/express-mpesa';
import {Utils} from '../../Common/utils/utils';
import {ServiceService} from '../../Common/service.service';
import {ServicePackageContent} from '../../Models/Packages/service_package';
import {Subscription} from '../../Models/Subscriptions/subscription_list_model';

@Component({
    selector: 'app-mpesa',
    templateUrl: './mpesa.component.html',
    encapsulation: ViewEncapsulation.None
})
export class MpesaComponent implements OnInit {

    heading = 'M-Pesa Stk';
    subheading = 'Lists all STK Transactions';
    icon = 'pe-7s-gleam icon-gradient bg-tempting-azure';

    datasource: Content[];
    totalRecords: number;
    loading: boolean;
    requestModel: StkPushRequestModel;
    submitted: boolean;
    clientDialog: boolean;
    errorOnCreation: boolean;
    errorMessage: any;
    subscriptionPlan: any[];
    serviceType: any[];
    paymentTypes: any[];
    transactionLoadingStatus: boolean;
    packageContents: ServicePackageContent[];
    filteredPackages: any[];
    subscriptionData: Subscription;
    private selectedPrice: number;

    constructor(
        private mps: MpesaService,
        private utils: Utils,
        private service: ServiceService
    ) {
    }

    ngOnInit(): void {
        this.loadTransactions();
        this.loadStaticDropDowns();
    }

    loadTransactions() {
        this.transactionLoadingStatus = true;
        this.mps.GetPagedExpressTransactions()
            .subscribe((data) => {
                this.datasource = data.content;
                this.totalRecords = data.totalElements;
                this.loading = false;
                this.transactionLoadingStatus = false;
            },(err) => {
                this.utils.showError(`Error Encountered ${err}`)
            }, () => {
                this.transactionLoadingStatus = false;
            });
    }

    openNew() {
        this.subscriptionData = ({} as any) as Subscription;
        this.requestModel = ({} as any) as StkPushRequestModel;
        this.submitted = false;
        this.clientDialog = true;
        this.errorOnCreation = false;
    }

    hideDialog() {
        this.clientDialog = false;
        this.submitted = false;
        this.errorOnCreation = false;
    }

    requestStkPush() {
        this.submitted = true;
        this.requestModel.idNumber = '29000000'
        if(this.subscriptionData.serviceCode.toLowerCase().includes(String(this.requestModel.payableAmount))){
            return this.mps.SendStkPush(JSON.stringify(this.requestModel))
                .subscribe((clientResponse) => {
                    if (clientResponse.success) {
                        this.utils.showSuccess(clientResponse.msg);
                        this.hideDialog();
                        this.utils.showSuccess('Successfully Loaded transactionStatus');
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
        }else{
            this.errorOnCreation = true;
            this.errorMessage = 'Price do not Match the selected Product'
            return;
        }
    }

    updateTransactionStatus(checkoutRequestId: string) {
        this.transactionLoadingStatus = true;
        this.checkTransactionStatus(checkoutRequestId)
            .then(() => {
                this.utils.showSuccess('Successfully Loaded transactionStatus');
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
        this.loadPackages();
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

    loadPackages() {
        this.transactionLoadingStatus = true;
        this.service.loadPackagesDataList().then(
            (data) => {
                this.packageContents = data;
            }).finally(() => {
            this.transactionLoadingStatus = false;
        });
    }

    searchServicePackage($searchEvent) {
        let filtered: any[] = [];
        let query = $searchEvent.query;
        for (let i = 0; i < this.packageContents.length; i++) {
            let plans = this.packageContents[i].code;
            if (plans.toLowerCase().includes(query.toLowerCase())) {
                filtered.push(plans);
            }
        }
        this.filteredPackages = filtered;
    }


    getSelectedPlan(value) {
        for (let i = 0; i < this.packageContents.length; i++) {
            let account = this.packageContents[i].code;
            if (account.toLowerCase().includes(value.toLowerCase())) {
                this.selectedPrice = this.packageContents[i].price
            }

            if(this.packageContents[i].code.length < 4){
                this.selectedPrice = 0
            }
        }
        this.requestModel.payableAmount = this.selectedPrice;
    }

    activateEmpty() {
        this.requestModel.payableAmount = null;
    }
}
