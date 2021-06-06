import {Component, OnInit} from '@angular/core';
import {PaybillService} from './paybill.service';
import {Content} from '../../../Models/C2B/c2bResponseModel';
import {Utils} from 'src/app/Common/utils/utils';
import {interval, Subscription} from 'rxjs';


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

    private subscription: Subscription;
    source = interval(60000);

    constructor(private paybillService: PaybillService, private utils: Utils) {
    }

    ngOnInit(): void {
        this.activateLoadingMask()
        this.loadConfirmationTransactions();
        this.refreshTable();
    }

    refreshTable(){
        this.subscription = this.source.subscribe(val => this.loadConfirmationTransactions());
    }
    ngOnDestroy(){
        this.subscription && this.subscription.unsubscribe();
    }

    loadConfirmationTransactions() {
        return this.paybillService.GetPagedC2BConfirmationTransactions()
            .subscribe((data) => {
                    this.datasource = data.content;
                    this.totalRecords = data.totalElements;
                }, (err) => {
                    this.utils.showError(`Error Was encountered listing confirmations  ${err}`);
                }, () => {
                    this.deactivateLoadingMask();
                }
            );
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
