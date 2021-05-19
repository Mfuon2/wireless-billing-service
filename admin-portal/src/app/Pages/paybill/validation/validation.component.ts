import { Component, OnInit } from '@angular/core';
import {Content} from '../../../Models/C2B/c2bResponseModel';
import {PaybillService} from '../confirmation/paybill.service';
import {Utils} from '../../../Common/utils/utils';

@Component({
  selector: 'app-validation',
  templateUrl: './validation.component.html',
  styleUrls: ['./validation.component.sass']
})
export class ValidationComponent implements OnInit {


  heading = 'Validation Logs';
  subheading = 'Lists all validations';
  icon = 'pe-7s-like2 icon-gradient bg-tempting-azure';

  paybillData: Content
  datasource: Content[];
  totalRecords: number;
  loading: boolean;
  paybillDialog: boolean;
  errorOnCreation: boolean;
  errorMessage: string;
  transactionLoadingStatus: boolean;
  submitted: boolean;

  constructor(private paybillService: PaybillService, private utils: Utils) {
  }

  ngOnInit(): void {
    this.loadValidationTransactions();
  }

  loadValidationTransactions() {
    return this.paybillService.GetPagedC2BValidationTransactions()
        .subscribe((data) => {
              this.datasource = data.content;
              this.totalRecords = data.totalElements;
              this.loading = false;
              this.transactionLoadingStatus = false;
            }, (err) => {
              this.utils.showError(`Error Was encountered listing validations  ${err}`);
            }, () => {
              this.transactionLoadingStatus = false;
              this.loading = false;
            }
        );
  }

}
