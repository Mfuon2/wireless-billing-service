import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import { Utils } from 'src/app/Common/utils/utils';
import {WalletService} from './wallet.service';
import {Content, WalletRequestModel} from '../../Models/Wallet/wallet';
import {ClientsService} from '../clients/clients.service';
import {ClientContent} from '../../Models/Clients/clients';
import {ServiceService} from '../../Common/service.service';
import {LoggerService} from '../../Common/logger.service';

@Component({
    selector: 'app-wallet',
    templateUrl: './wallet.component.html',
    encapsulation: ViewEncapsulation.None
})
export class WalletComponent implements OnInit {

    heading = 'Wallet';
    subheading = 'Lists all Wallet Account Balances';
    icon = 'pe-7s-users icon-gradient bg-tempting-azure';

    walletData: WalletRequestModel;
    datasource: Content[];
    totalRecords: number;
    loading: boolean;
    submitted: boolean;
    walletDialog: boolean;
    serviceType: any[];
    errorOnCreation: boolean;
    errorMessage: string;
    findErrorMessage: string;
    findError: boolean;
    clientContents: ClientContent[];
  filteredClients: any[];
    checkAccountNumber: boolean = false;
    selectedClient: string = '';
    userSelectionBadge: string = 'danger';

    constructor(
        private walletService: WalletService,
        private clientService: ClientsService,
        private utils: Utils,
        private log: LoggerService,
        private service: ServiceService
    ) {
        this.serviceType = [
            {label: 'PRE PAID SERVICE', value: 'PRE_PAID'},
            {label: 'ADHOC SERVICE', value: 'ADHOC'},
            {label: 'POST PAID SERVICE', value: 'POST_PAID'}
        ];
        this.findError = false;
        this.loadClients();
    }

    ngOnInit(): void {
        this.loading = true;
        this.loadWallets();
    }

    loadWallets() {
        return this.walletService.GetPagedWallets(0, 1000)
            .subscribe((data) => {
                this.datasource = data.content;
                this.totalRecords = data.totalElements;
                this.loading = false;
            });
    }

    saveWallet() {
        this.submitted = true;
        return this.walletService.SaveWallet(JSON.stringify(this.walletData))
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
                this.reload();
            });
    }

    openNew() {
        // @ts-ignore
        this.walletData = {};
        this.submitted = false;
        this.walletDialog = true;
        this.findErrorMessage = '';
        this.findError = false;
        this.selectedClient = '';
        this.userSelectionBadge = 'danger'
    }

    hideDialog() {
        this.walletDialog = false;
        this.submitted = false;
        this.selectedClient = '';
        this.userSelectionBadge = 'danger'
    }

    loadClients() {
        this.service.loadClientDataList().then(
            (data) => {
                this.clientContents = data;
            }).finally(() => {
        });
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
