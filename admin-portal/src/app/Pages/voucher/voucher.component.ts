import {Component, OnInit, ViewChild} from '@angular/core';
import {Utils} from '../../Common/utils/utils';
import {VoucherService} from './voucher.service';
import {environment} from '../../../environments/environment';
import {ClaimModel, CSVRecord} from '../../Models/Voucher/voucher';
import {MessageService} from 'primeng/api';

@Component({
    selector: 'app-voucher',
    templateUrl: './voucher.component.html',
    styleUrls: ['./voucher.component.sass']
})
export class VoucherComponent implements OnInit {

    heading = 'Vouchers';
    subheading = 'Manage All Vouchers';
    icon = 'pe-7s-diamond icon-gradient bg-tempting-azure';

    uploadedFiles: any;
    transactionLoadingStatus: boolean;
    submitted: any;
    errorOnCreation: any;
    errorMessage: any;
    voucherDialog: any;
    loading: any;
    datasource: any;
    totalRecords: any;
    sizeLabel: string;
    fileSizeError: string;

    baseurl: string = environment.baseurl;
    records: any[];
    @ViewChild('csvReader') csvReader: any;
    claimDialog: any;
    responseModel: ClaimModel;
    messageToken: string;
    claimReason: any;

    constructor(private util: Utils, private voucherService: VoucherService, private messageService: MessageService) {
    }

    ngOnInit(): void {
        this.activateLoadingMask();
        this.loadVouchers();
        this.responseModel = ({} as any) as ClaimModel;
        this.claimReason = [
            {label: 'M-pesa not logged in our system', value: 'MISSING_MPESA_LOG'},
            {label: 'M-pesa logged but voucher SMS missing', value: 'MISSING_SMS_LOG'},
            {label: 'Unable to auto-send via SMS', value: 'SMS_ISSUE'}
        ];
    }

    onUploadCsvFile() {
        console.log(this.uploadedFiles);
        this.activateLoadingMask();
        return this.voucherService.UploadVoucher(this.uploadedFiles)
            .subscribe((data) => {
                console.log(JSON.stringify(data));
            }, (error) => {
                this.errorOnCreation = true;
                this.errorMessage = error;
                this.util.showError(error);
            }, () => {
                this.deactivateLoadingMask();
            });
    }

    uploadListener($event: any): void {
        this.transactionLoadingStatus = true;
        let text = [];
        let files = $event.srcElement.files;
        const fileSize = files[0].size;
        if (fileSize > 512000) {
            this.util.showError(`File Exceeds Upload size of ${512} KB`);
            this.showFileError(`File Exceeds Upload size of ${512} KB`);
            this.transactionLoadingStatus = false;
            return;
        } else {
            this.sizeLabel = `Selected ${files[0].name.toUpperCase()} of size ${files[0].size / 1000} Kb`;
            if (this.isValidCSVFile(files[0])) {
                let input = $event.target;
                this.uploadedFiles = input.files[0];
                let reader = new FileReader();
                reader.readAsText(input.files[0]);
                reader.onload = () => {
                    let csvData = reader.result;
                    let csvRecordsArray = (<string> csvData).split(/\r\n|\n/);
                    let headersRow = this.getHeaderArray(csvRecordsArray);
                    this.records = this.getDataRecordsArrayFromCSVFile(csvRecordsArray, headersRow.length);
                };

                let me = this;
                reader.onerror = function() {
                    me.util.showError('error occurred while reading file!');
                };
            } else {
                this.util.showError('Please import valid .csv file.');
                this.fileReset();
            }
        }
    }

    isValidCSVFile(file: any) {
        return file.name.endsWith('.csv');
    }

    getHeaderArray(csvRecordsArr: any) {
        let headers = (<string> csvRecordsArr[0]).split(',');
        let headerArray = [];
        for (let j = 0; j < headers.length; j++) {
            headerArray.push(headers[j]);
        }
        return headerArray;
    }

    fileReset() {
        this.csvReader.nativeElement.value = '';
        this.records = [];
    }

    getDataRecordsArrayFromCSVFile(csvRecordsArray: any, headerLength: any) {
        let csvArr = [];

        for (let i = 1; i < csvRecordsArray.length; i++) {
            let currentRecord = (<string> csvRecordsArray[i]).split(',');
            if (currentRecord.length == headerLength) {
                let csvRecord: CSVRecord = new CSVRecord();
                csvRecord.voucherId = currentRecord[0].trim();
                csvRecord.portal = currentRecord[1].trim();
                csvRecord.plan = currentRecord[2].trim();
                csvRecord.creationTime = currentRecord[3].trim();
                csvRecord.expiryTime = currentRecord[5].trim();
                csvArr.push(csvRecord);
            }
        }
        this.util.showInfo(`Done Loading Vouchers`);
        this.transactionLoadingStatus = false;
        return csvArr;
    }

    loadVouchers() {
        this.voucherService.GetUploadedVouchers()
            .subscribe((data) => {
                this.datasource = data.content;
                this.totalRecords = data.totalElements;
            }, (err) => {
                this.util.showError(`Error Encountered ${err}`);
            }, () => {
                this.deactivateLoadingMask();
            });
    }

    showFileError(msg) {
        this.messageService.add({severity: 'error', summary: 'Service Message', detail: `${msg}`});
    }

    openFileUploadDialog() {
        this.sizeLabel = '';
        this.fileSizeError = '';
        this.records = [];
        this.submitted = false;
        this.voucherDialog = true;
    }

    hideDialog() {
        this.records = [];
        this.voucherDialog = false;
        this.submitted = false;
        this.claimDialog = false;
    }

    deactivateLoadingMask() {
        this.transactionLoadingStatus = false;
        this.loading = false;
    }

    activateLoadingMask() {
        this.transactionLoadingStatus = true;
        this.loading = true;
    }

    claimVoucher(id: number, voucher: string, expiry: string) {
        this.responseModel = ({} as any) as ClaimModel;
        this.claimDialog = true;
        this.responseModel.mpesaMessage = this.messageToken;
        this.responseModel.msisdn = `+254`;
    }

    claimVoucherSms() {
        this.submitted = true;
        console.log(this.responseModel)
        this.transactionLoadingStatus = true;
        if (this.responseModel.msisdn.length < 7 || this.responseModel.mpesaMessage.length < 5 || this.responseModel.remarks.length < 5) {
            this.errorOnCreation = true;
            this.errorMessage = `Fields are required MPesa Code, Phone and Remarks`;
            this.transactionLoadingStatus = false;
            return;
        } else if(this.responseModel.reason === undefined) {
            this.errorOnCreation = true;
            this.errorMessage = `Please Select Reason`;
            this.transactionLoadingStatus = false;
        }
            else {
            this.submitted = true;
            return this.voucherService.ClaimVoucher(JSON.stringify(this.responseModel))
                .subscribe((res) => {
                    if (res.success) {
                        this.util.showSuccess(res.msg);
                        this.hideDialog();
                    } else {
                        this.errorOnCreation = true;
                        this.errorMessage = res.msg;
                        this.util.showError(res.msg);
                    }
                }, (error) => {
                    this.errorOnCreation = true;
                    this.errorMessage = error;
                    this.util.showError(error);
                    this.transactionLoadingStatus = false;
                }, () => {
                    this.transactionLoadingStatus = false;
                    this.reload();
                });
        }
    }

    private reload() {
        this.ngOnInit();
    }
}
