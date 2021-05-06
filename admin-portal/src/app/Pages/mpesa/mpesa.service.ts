import { Injectable } from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {HttpErrorHandler} from '../../HttpErrorHandler';
import {Observable} from 'rxjs';
import {ClientCreationModel, ClientsResponseDto} from '../../Models/Clients/clients';
import {catchError, retry} from 'rxjs/operators';
import {
  ExpressListingModel,
  MpesaTransStatusModel,
  StkResponseModel,
  TransactionStatusResponseModel
} from '../../Models/Mpesa/express-mpesa';

@Injectable({
  providedIn: 'root'
})
export class MpesaService {

  baseurl: string = environment.baseurl;
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin':'*'
    })
  };

  constructor(private http: HttpClient, private error : HttpErrorHandler) {
  }

  GetPagedExpressTransactions(): Observable<ExpressListingModel> {
    return this.http.get<ExpressListingModel>(`${this.baseurl}/express/paged?page=0&size=99999&sort=asc`)
        .pipe(
            retry(1),
            catchError(err => this.error.errorHandler(err))
        );
  }

  GetTransactionDetails(checkoutRequestId: string): Observable<MpesaTransStatusModel> {
    return this.http.get<MpesaTransStatusModel>(`${this.baseurl}/express/transaction-details?checkoutRequestId=${checkoutRequestId}`)
        .pipe(
            retry(1),
            catchError(err => this.error.errorHandler(err))
        );
  }

  SendStkPush(clientData: string): Observable<StkResponseModel>{
    return this.http.post<StkResponseModel>(`${this.baseurl}/express/payment-request`,clientData,this.httpOptions)
        .pipe(
            retry(1),
            catchError(err => this.error.errorHandler(err))
        )
  }

  async GetTransactionStatus(checkoutRequestId: any) {
    return this.http.get<TransactionStatusResponseModel>(`${this.baseurl}/express/transaction-status?checkoutRequestId=${checkoutRequestId}`)
        .pipe(
            retry(1),
            catchError(err => this.error.errorHandler(err))
        );
  }
}
