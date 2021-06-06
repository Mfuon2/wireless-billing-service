import {Component, OnInit} from '@angular/core';
import {Content} from '../../Models/sms/smsModel';
import {MessageService} from 'primeng/api';
import {Utils} from '../../Common/utils/utils';
import {SmsService} from './sms.service';
import {interval, Subscription} from 'rxjs';

@Component({
    selector: 'app-sms',
    templateUrl: './sms.component.html',
    styleUrls: ['./sms.component.sass']
})
export class SmsComponent implements OnInit {
    heading = 'SMS Logs';
    subheading = 'Lists all SMS sent to Clients';
    icon = 'pe-7s-mail icon-gradient bg-tempting-azure';

    subscription: Subscription;
    source = interval(60000);


    transactionLoadingStatus: boolean;
    loading: boolean;
    totalRecords: number;
    datasource: Content[];

    smsData: Content;
    submitted: boolean;
    smsDialog: boolean;
    errorOnCreation: boolean;
    errorMessage: string;
    iconDefault: string = 'pi pi-replay';


    constructor(private service: SmsService, private messageService: MessageService, private utils: Utils) {
    }

    ngOnInit(): void {
        this.activateLoadingMask();
        this.loadSms()
        this.refreshTable()
    }

    hideDialog() {
        this.smsDialog = false;
        this.submitted = false;
    }

    refreshTable(){
            this.subscription = this.source.subscribe(val => this.loadSms());
    }
    ngOnDestroy(){
        console.log('DESTROYED');
        this.subscription && this.subscription.unsubscribe();
    }

    openNew() {
        this.smsData = ({} as any) as Content;
        this.submitted = false;
        this.smsDialog = true;
    }

    loadSms() {
        return this.service.GetPagedSms()
            .subscribe((data) => {
                this.datasource = data.content;
                this.totalRecords = data.totalElements;
            }, (err) => {
                this.utils.showError(`Error Was encountered listing Sms Logs  ${err}`);
            }, () => {
                this.deactivateLoadingMask();
            });
    }

    deactivateLoadingMask() {
        this.transactionLoadingStatus = false;
        this.loading = false;
    }

    activateLoadingMask() {
        this.transactionLoadingStatus = true;
        this.loading = true;
    }

    resendMessage(id: number | any) {
        this.transactionLoadingStatus = true;
        return this.service.ReSendSms(id)
            .subscribe((data) => {
                this.utils.showSuccess(`${data.msg}`);
            }, (err) => {
                this.utils.showError(`Error Was encountered resending sms  ${err}`);
                this.deactivateLoadingMask();
            }, () => {
                this.deactivateLoadingMask();
            });
    }
}
