import {Component, OnInit} from '@angular/core';
import {Content} from '../../Models/Equity/equityResponseModel';
import {Utils} from '../../Common/utils/utils';
import {EquityService} from './equity.service';


@Component({
    selector: 'app-equity',
    templateUrl: './equity.component.html',
    styleUrls: ['./equity.component.sass']
})
export class EquityComponent implements OnInit {
    heading = 'Equity Logs';
    subheading = 'Lists all Equity logs';
    icon = 'pe-7s-diamond icon-gradient bg-tempting-azure';

    datasource: Content[];
    totalRecords: number;
    loading: boolean;
    errorOnCreation: boolean;
    errorMessage: string;
    transactionLoadingStatus: boolean;
    submitted: boolean;

    constructor(private equityService: EquityService, private utils: Utils) {
    }

    ngOnInit(): void {
        this.activateLoadingMask();
        this.loadEquityTransactions();
    }

    loadEquityTransactions() {
        return this.equityService.GetEquityTransactions()
            .subscribe((data) => {
                    this.datasource = data.content;
                    this.totalRecords = data.totalElements;
                }, (err) => {
                    this.utils.showError(`Error Was encountered listing equity transaction logs  ${err}`);
                }, () => {
                    this.deactivateLoadingMask();
                }
            );
    }

    deactivateLoadingMask() {
        this.transactionLoadingStatus = false;
        this.loading = false;
    }

    activateLoadingMask() {
        this.transactionLoadingStatus = true;
        this.loading = true;
    }
}
