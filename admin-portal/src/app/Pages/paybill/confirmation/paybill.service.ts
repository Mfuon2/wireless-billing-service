import { Injectable } from '@angular/core';
import {environment} from '../../../../environments/environment';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {HttpErrorHandler} from '../../../HttpErrorHandler';
import {Observable} from 'rxjs';
import {ClientsResponseDto} from '../../../Models/Clients/clients';
import {catchError, retry} from 'rxjs/operators';
import {C2BResponseModel} from '../../../Models/C2B/c2bResponseModel';

@Injectable({
  providedIn: 'root'
})
export class PaybillService {
  baseurl: string = environment.baseurl;
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin':'*'
    })
  };

  constructor(private http: HttpClient, private error : HttpErrorHandler) {
  }

  GetPagedC2BConfirmationTransactions(): Observable<C2BResponseModel> {
    return this.http.get<C2BResponseModel>(`${this.baseurl}/c2b/confirmations/paged?page=0&size=99999999&sort=asc`)
        .pipe(
            retry(1),
            catchError(err => this.error.errorHandler(err))
        );
  }

  GetPagedC2BValidationTransactions(): Observable<C2BResponseModel> {
    return this.http.get<C2BResponseModel>(`${this.baseurl}/c2b/validations/paged?page=0&size=99999999&sort=asc`)
        .pipe(
            retry(1),
            catchError(err => this.error.errorHandler(err))
        );
  }
}
