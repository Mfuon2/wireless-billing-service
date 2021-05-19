import { Injectable } from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {HttpErrorHandler} from '../../HttpErrorHandler';
import {Observable} from 'rxjs';
import {catchError, retry} from 'rxjs/operators';
import {EquityResponseModel} from '../../Models/Equity/equityResponseModel';

@Injectable({
  providedIn: 'root'
})
export class EquityService {
  baseurl: string = environment.baseurl;
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin':'*'
    })
  };

  constructor(private http: HttpClient, private error : HttpErrorHandler) {
  }

  GetEquityTransactions(): Observable<EquityResponseModel> {
    return this.http.get<EquityResponseModel>(`${this.baseurl}/equity/paged?page=0&size=99999999&sort=asc`)
        .pipe(
            retry(1),
            catchError(err => this.error.errorHandler(err))
        );
  }
}
