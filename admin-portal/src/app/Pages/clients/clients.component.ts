import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {ClientsService} from './clients.service';
import {Content} from '../../Models/Clients/clients';
import {MessageService, SortEvent} from 'primeng/api';
import {Utils} from 'src/app/Common/utils/utils';

@Component({
    selector: 'app-clients',
    templateUrl: './clients.component.html',
    encapsulation: ViewEncapsulation.None
})
export class ClientsComponent implements OnInit {
    heading = 'Clients Accounts';
    subheading = 'Lists all Clients Accounts';
    icon = 'pe-7s-users icon-gradient bg-tempting-azure';

    clientData: Content;
    datasource: Content[];
    totalRecords: number;
    cols: any[];
    loading: boolean;
    submitted: boolean;
    clientDialog: boolean;
    serviceTypes: any[];
    errorOnCreation: boolean;
    errorMessage: string;
    transactionLoadingStatus: boolean;

    constructor(private clientService: ClientsService, private messageService: MessageService, private utils: Utils) {

        this.serviceTypes = [
            {label: 'PRE PAID SERVICE', value: 'PRE_PAID'},
            {label: 'ADHOC SERVICE', value: 'ADHOC'},
            {label: 'POST PAID SERVICE', value: 'POST_PAID'}
        ];
    }

    ngOnInit(): void {
        this.activateLoadingMask()
        this.loadClients();
    }

    // Get clients list
    loadClients() {
        return this.clientService.GetPagedClients(0, 1000)
            .subscribe((data) => {
                this.datasource = data.content;
                this.totalRecords = data.totalElements;
            }, (err) => {
                this.utils.showError(`Error Was encountered listing Clients  ${err}`)
            }, () => {
                this.deactivateLoadingMask()
            });
    }

    saveClientAccount() {
        this.submitted = true;
        this.activateLoadingMask()
        return this.clientService.SaveClientsAccount(JSON.stringify(this.clientData))
            .subscribe((clientResponse) => {
                if (clientResponse.success) {
                    this.utils.showSuccess(clientResponse.msg);
                    this.hideDialog();
                } else {
                    this.errorOnCreation = true;
                    this.errorMessage = clientResponse.msg;
                    this.utils.showError(clientResponse.msg);
                }
            }, (error) => {
                this.errorOnCreation = true;
                this.errorMessage = error;
                this.utils.showError(error);
                this.deactivateLoadingMask()
            }, () => {
                this.deactivateLoadingMask()
                this.reload();
            });
    }

    openNew() {
        // @ts-ignore
        this.clientData = {};
        this.submitted = false;
        this.clientDialog = true;
    }

    hideDialog() {
        this.clientDialog = false;
        this.submitted = false;
    }

    customSort(event: SortEvent) {
        event.data.sort((data1, data2) => {
            let value1 = data1[event.field];
            let value2 = data2[event.field];
            let result = null;

            if (value1 == null && value2 != null) {
                result = -1;
            } else if (value1 != null && value2 == null) {
                result = 1;
            } else if (value1 == null && value2 == null) {
                result = 0;
            } else if (typeof value1 === 'string' && typeof value2 === 'string') {
                result = value1.localeCompare(value2);
            } else {
                result = (value1 < value2) ? -1 : (value1 > value2) ? 1 : 0;
            }

            return (event.order * result);
        });
    }

    private reload() {
        this.ngOnInit()
    }

    deactivateLoadingMask() {
        this.transactionLoadingStatus = false;
        this.loading = false;
    }

    activateLoadingMask(){
        this.transactionLoadingStatus = true;
        this.loading = true;
    }
}
