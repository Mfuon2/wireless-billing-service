import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {ServicePackagesService} from './service-packages.service';
import {Utils} from '../Components/Utils';
import {Content} from '../../Models/Packages/service_package';

@Component({
    selector: 'app-service-packages',
    templateUrl: './service-packages.component.html',
    encapsulation: ViewEncapsulation.None
})
export class ServicePackagesComponent implements OnInit {
    heading = 'Service Packages';
    subheading = 'Lists all Service Packages';
    icon = 'pe-7s-network icon-gradient bg-tempting-azure';
    data: any;
    servicePlanData: Content;
    datasource: Content[];
    totalRecords: number;
    cols: any[];
    loading: boolean;
    submitted: boolean;
    servicePlanDialog: boolean;
    cycleTypes: any[];
    errorOnCreation: boolean;
    errorMessage: string;

    constructor(private packageService: ServicePackagesService, private utils: Utils) {
        this.cycleTypes = [
            {label: 'WEEKLY PACKAGE', value: 'WEEKLY'},
            {label: 'ANNUAL PACKAGE', value: 'ANNUAL'},
            {label: 'ADHOC PACKAGE', value: 'ADHOC'},
            {label: 'MONTHLY PACKAGE', value: 'MONTHLY'}
        ];
    }

    ngOnInit(): void {
        this.loading = true;
        this.loadClients();
    }

    reload() {
        this.ngOnInit();
    }

    loadClients() {
        return this.packageService.GetPagedServices()
            .subscribe((data) => {
                this.datasource = data.content;
                this.totalRecords = data.totalElements;
                this.loading = false;
            });
    }

    saveServicePackageUi() {
        this.submitted = true;
        return this.packageService.SaveServicePackage(JSON.stringify(this.servicePlanData))
            .subscribe((data) => {
                if (data.success) {
                    this.utils.showSuccess(data.msg);
                    this.hideDialog();
                } else {
                    this.errorOnCreation = true;
                    this.errorMessage = data.msg;
                    this.utils.showError(data.msg);
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
        this.servicePlanData = {};
        this.submitted = false;
        this.servicePlanDialog = true;
    }

    hideDialog() {
        this.servicePlanDialog = false;
        this.submitted = false;
    }

}
