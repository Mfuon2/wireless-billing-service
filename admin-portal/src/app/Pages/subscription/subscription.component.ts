import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {Content, Subscription} from '../../Models/Subscriptions/subscription_list_model';
import { Utils } from '../Components/Utils';
import {SubscriptionService} from './subscription.service';


@Component({
  selector: 'app-subscription',
  templateUrl: './subscription.component.html',
  encapsulation: ViewEncapsulation.None
})
export class SubscriptionComponent implements OnInit {

  heading = 'Client Subscriptions';
  subheading = 'Lists all Clients Subscriptions';
  icon = 'pe-7s-exapnd2 icon-gradient bg-tempting-azure';
  subscriptionData: Subscription;
  datasource: Content[];
  totalRecords: number;
  cols: any[];
  loading: boolean;
  submitted: boolean;
  subscriptionDialog: boolean;
  subscriptionPlan: any[];
  errorOnCreation: boolean;
  errorMessage: string;

  constructor(private subscriptionService: SubscriptionService, private utils: Utils) {
    this.subscriptionPlan = [
      {label: 'PERSONAL',value:'PERSONAL'},
      {label: 'DEMO',value:'DEMO'},
      {label: 'PLATINUM',value:'PLATINUM'},
      {label: 'BUSINESS',value:'BUSINESS'}
    ]
  }

  ngOnInit(): void {
    this.loading = true;
    this.loadSubscriptions();
  }

  loadSubscriptions() {
    return this.subscriptionService.GetPagedSubscriptions()
        .subscribe((data) => {
          this.datasource = data.content;
          this.totalRecords = data.totalElements;
          this.loading = false;
        });
  }

  saveSubscription() {
    this.submitted = true;
    return this.subscriptionService.SaveSubscription(JSON.stringify(this.subscriptionData))
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
    this.subscriptionData = {};
    this.submitted = false;
    this.subscriptionDialog = true;
  }

  hideDialog() {
    this.subscriptionDialog = false;
    this.submitted = false;
  }

  private reload() {
    this.ngOnInit()
  }
}
