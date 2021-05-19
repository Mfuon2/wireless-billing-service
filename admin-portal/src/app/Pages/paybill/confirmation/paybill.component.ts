import {Component, OnInit} from '@angular/core';
import {PaybillService} from './paybill.service';
import {Content} from '../../../Models/C2B/c2bResponseModel';
import {Utils} from 'src/app/Common/utils/utils';


@Component({
    selector: 'app-paybill',
    templateUrl: './paybill.component.html',
    styleUrls: ['./paybill.component.sass']
})
export class PaybillComponent implements OnInit {

    heading = 'Confirmation Logs';
    subheading = 'Lists all Confirmation logs';
    icon = 'pe-7s-news-paper icon-gradient bg-tempting-azure';

    datasource: Content[];
    totalRecords: number;
    loading: boolean;
    errorOnCreation: boolean;
    errorMessage: string;
    transactionLoadingStatus: boolean;
    submitted: boolean;

    constructor(private paybillService: PaybillService, private utils: Utils) {
    }

    ngOnInit(): void {
        this.loadConfirmationTransactions();
    }

    loadConfirmationTransactions() {
        return this.paybillService.GetPagedC2BConfirmationTransactions()
            .subscribe((data) => {
                this.datasource = data.content;
                this.totalRecords = data.totalElements;
                this.loading = false;
                this.transactionLoadingStatus = false;
            }, (err) => {
                this.utils.showError(`Error Was encountered listing confirmations  ${err}`);
            }, () => {
                this.transactionLoadingStatus = false;
                this.loading = false;
            }
            );
    }

}
