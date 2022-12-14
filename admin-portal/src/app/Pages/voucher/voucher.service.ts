import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {HttpErrorHandler} from '../../HttpErrorHandler';
import {Observable} from 'rxjs';
import {catchError, retry} from 'rxjs/operators';
import {ClaimedResponse, VouchersListingDto} from '../../Models/Voucher/voucher';
import {StkResponseModel} from '../../Models/Mpesa/express-mpesa';

@Injectable({
    providedIn: 'root'
})
export class VoucherService {

    baseurl: string = environment.baseurl;
    httpOptions = {
        headers: new HttpHeaders({
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': '*'
        })
    };

    constructor(private http: HttpClient, private error: HttpErrorHandler) {
    }

    GetUploadedVouchers(): Observable<VouchersListingDto> {
        return this.http.get<VouchersListingDto>(`${this.baseurl}/voucher/temp/paged?page=0&size=99999&sort=asc`)
            .pipe(
                retry(1),
                catchError(err => this.error.errorHandler(err))
            );
    }

    UploadVoucher(file: any) {
        let formData: FormData = new FormData();
        formData.append('file', file);
        let options = {
            headers: new HttpHeaders({
                'Content-Type': 'multipart/form-data',
                'Access-Control-Allow-Origin': '*',
                'Accept': '*/*'
            })
        };
        return this.http.post(`${this.baseurl}/files/upload`, formData, options)
            .pipe(
                retry(1),
                catchError(err => this.error.errorHandler(err))
            );
    }

    ClaimVoucher(voucherData){
        return this.http.post<ClaimedResponse>(`${this.baseurl}/voucher/claim`,voucherData,this.httpOptions)
            .pipe(
                retry(1),
                catchError(err => this.error.errorHandler(err))
            )
    }
}
