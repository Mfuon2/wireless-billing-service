import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {Content, SubscriptionD} from '../../Models/Subscriptions/subscription_list_model';
import { Utils } from 'src/app/Common/utils/utils';
import {SubscriptionService} from './subscription.service';
import {ClientsService} from '../clients/clients.service';
import {ServicePackagesService} from '../service-packages/service-packages.service';
import {ServicePackageContent} from '../../Models/Packages/service_package';
import {ClientContent} from '../../Models/Clients/clients';
import {ServiceService} from '../../Common/service.service';
import {LoggerService} from '../../Common/logger.service';


@Component({
    selector: 'app-subscription',
    templateUrl: './subscription.component.html',
    encapsulation: ViewEncapsulation.None
})
export class SubscriptionComponent implements OnInit {

    heading = 'Client Subscriptions';
    subheading = 'Lists all Clients Subscriptions';
    icon = 'pe-7s-exapnd2 icon-gradient bg-tempting-azure';
    subscriptionData: SubscriptionD;
    datasource: Content[];
    totalRecords: number;
    cols: any[];
    loading: boolean;
    submitted: boolean;
    subscriptionDialog: boolean;
    subscriptionPlan: any[];
    errorOnCreation: boolean;
    errorMessage: string;
    findError: boolean;
    findErrorMessage: string;
    filteredPackages: any[];
    filteredClients: any[];
    packageContents: ServicePackageContent[];
    clientContents: ClientContent[];
    transactionLoadingStatus: boolean;
    selectedClient: string;
    userSelectionBadge: any = 'danger';


    constructor(
        private subscriptionService: SubscriptionService,
        private packagesService: ServicePackagesService,
        private clientService: ClientsService,
        private service: ServiceService,
        private log: LoggerService,
        private utils: Utils
    ) {
        this.subscriptionPlan = [
            {label: 'PERSONAL', value: 'PERSONAL'},
            {label: 'DEMO', value: 'DEMO'},
            {label: 'PLATINUM', value: 'PLATINUM'},
            {label: 'BUSINESS', value: 'BUSINESS'}
        ];
    }

    ngOnInit(): void {
        this.loading = true;
        this.loadSubscriptions();
        this.loadClients();
        this.loadPackages();
    }

    loadSubscriptions() {
        this.transactionLoadingStatus = true;
        return this.subscriptionService.GetPagedSubscriptions()
            .subscribe((data) => {
                this.datasource = data.content;
                this.totalRecords = data.totalElements;
                this.loading = false;
            },e => {},() =>
            {this.transactionLoadingStatus = false});
    }

    saveSubscription() {
        this.transactionLoadingStatus = true;
        console.log(this.subscriptionData.subscriptionPlan);
        if ((this.subscriptionData.subscriptionPlan === null || this.subscriptionData.subscriptionPlan === undefined) &&
            (this.subscriptionData.accountNumber === null || this.subscriptionData.subscriptionPlan === undefined) &&
            (this.subscriptionData.serviceCode === null || this.subscriptionData.subscriptionPlan === undefined)) {
            this.submitted = true;
            this.errorMessage = `The following fields are required`;
            return this.errorMessage;
        } else {
            return this.subscriptionService.SaveSubscription(JSON.stringify(this.subscriptionData))
                .subscribe((res) => {
                    if (res.success) {
                        this.utils.showSuccess(res.msg);
                        this.hideDialog();
                    } else {
                        this.errorOnCreation = true;
                        this.errorMessage = res.msg;
                        this.utils.showError(res.msg);
                    }
                }, (error) => {
                    this.errorOnCreation = true;
                    this.errorMessage = error;
                    this.utils.showError(error);
                }, () => {
                    this.transactionLoadingStatus = false;
                    this.reload();
                });
        }
    }

    openNew() {
        // @ts-ignore
        this.subscriptionData = {};
        this.submitted = false;
        this.subscriptionDialog = true;
        this.selectedClient = '';
        this.userSelectionBadge = 'danger'
    }

    hideDialog() {
        this.subscriptionDialog = false;
        this.submitted = false;
        this.selectedClient = '';
        this.userSelectionBadge = 'danger'
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

    loadClients() {
        this.service.loadClientDataList().then(
            (data) => {
                this.clientContents = data;
            }).finally(() => {
        });
    }

    searchServicePackage($searchEvent) {
        let filtered: any[] = [];
        let query = $searchEvent.query;
        for (let i = 0; i < this.packageContents.length; i++) {
            let plans = this.packageContents[i].code;
            if (plans.toLowerCase().indexOf(query.toLowerCase()) == 0) {
                filtered.push(plans);
            }
        }
        this.filteredPackages = filtered;
    }

    searchClientAccount($searchEvent) {
        let filtered: any[] = [];
        let query = $searchEvent.query;
        for (let i = 0; i < this.clientContents.length; i++) {
            let plans = this.clientContents[i].accountNumber;
            if (plans.toLowerCase().includes(query.toLocaleString())) {
                filtered.push(plans);
            }
        }
        this.filteredClients = filtered;
    }

    private reload() {
        this.ngOnInit();
        this.selectedClient = '';
    }

    getClientAccount(value) {
        for (let i = 0; i < this.clientContents.length; i++) {
            let account = this.clientContents[i].accountNumber;
            if (account.toLowerCase().includes(value.toLowerCase())) {
                this.selectedClient = this.clientContents[i].accountName
                this.userSelectionBadge = 'success'
            }

            if(this.clientContents[i].accountName.length < 4){
                    this.selectedClient = 'No Account Selected'
                    this.userSelectionBadge = 'warning'
            }
        }
    }

    activateEmpty() {
            this.selectedClient = 'No Account Selected'
            this.userSelectionBadge = 'warning'
    }
}
