import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {Utils} from '../Components/Utils';
import {WalletService} from './wallet.service';
import {Content, WalletRequestModel} from '../../Models/Wallet/wallet';
import {event} from 'd3';
import {ClientsService} from '../clients/clients.service';

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
  cols: any[];
  loading: boolean;
  submitted: boolean;
  walletDialog: boolean;
  serviceType: any[];
  errorOnCreation: boolean;
  errorMessage: string;
  findErrorMessage: string;
  findError: boolean;

  constructor(private walletService: WalletService,private clientService: ClientsService, private utils: Utils) {
    this.serviceType = [
      {label: 'PRE PAID SERVICE', value: 'PRE_PAID'},
      {label: 'ADHOC SERVICE', value: 'ADHOC'},
      {label: 'POST PAID SERVICE', value: 'POST_PAID'}
    ];
    this.findError = false;
  }

  ngOnInit(): void {
    this.loading = true;
    this.loadWallets();
  }

  loadWallets() {
    return this.walletService.GetPagedWallets(0,1000)
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
          if(res.success){
            this.utils.showSuccess(res.msg)
            this.hideDialog()
          }else {
            this.errorOnCreation = true;
            this.errorMessage = res.msg
            this.utils.showError(res.msg)
          }
        }, (error) => {
          this.errorOnCreation = true;
          this.errorMessage = error
          this.utils.showError(error)
        }, () => {
          this.reload ()
        })
  }

  openNew() {
    // @ts-ignore
    this.walletData = {};
    this.submitted = false;
    this.walletDialog = true
    this.findErrorMessage = '';
    this.findError = false;
  }

  hideDialog() {
    this.walletDialog = false;
    this.submitted = false;
  }

  private reload() {
    this.ngOnInit()
  }

  loadServiceType($event : any) {
      this.loading = true
      this.clientService.GetClientAccount($event.target.value)
          .subscribe((data) => {
            if(data.data != null){
              this.findError = false;
              this.findErrorMessage = ''
              this.walletData.serviceType = data.data.serviceType
              this.loading = false;
            }else{
              this.findError = true;
              this.findErrorMessage = data.msg
              this.walletData.serviceType = ''
              this.utils.showError(data.msg)
            }
          });
  }
}
