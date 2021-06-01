import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {HttpErrorHandler} from '../../HttpErrorHandler';
import {ClientsResponseDto} from '../../Models/Clients/clients';
import {catchError, retry} from 'rxjs/operators';
import {ResendSmsResponseModel, SmsLogModel} from '../../Models/sms/smsModel';

@Injectable({
    providedIn: 'root'
})
export class SmsService {

    baseurl: string = environment.baseurl;
    httpOptions = {
        headers: new HttpHeaders({
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': '*'
        })
    };

    constructor(private http: HttpClient, private error: HttpErrorHandler) {
    }

    GetPagedSms() {
      return this.http.get<SmsLogModel>(`${this.baseurl}/sms/paged?page=${0}&size=${999999999}&sort=desc`)
          .pipe(
              retry(1),
              catchError(err => this.error.errorHandler(err))
          );
    }

    ReSendSms(id: number) {
        return this.http.get<ResendSmsResponseModel>(`${this.baseurl}/sms/resend?id=${id}`)
            .pipe(
                retry(1),
                catchError(err => this.error.errorHandler(err))
            );
    }
}
