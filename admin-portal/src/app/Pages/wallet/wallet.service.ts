import { Injectable } from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {HttpErrorHandler} from '../../HttpErrorHandler';
import {Observable} from 'rxjs';
import {catchError, retry} from 'rxjs/operators';
import {WalletListingDto, WalletResponseModel} from '../../Models/Wallet/wallet';

@Injectable({
  providedIn: 'root'
})
export class WalletService {

  baseurl = environment.baseurl;
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin':'*'
    })
  };

  constructor(private http: HttpClient, private error : HttpErrorHandler) {
  }

  GetPagedWallets(page: number, size: number): Observable<WalletListingDto> {
    return this.http.get<WalletListingDto>(`${this.baseurl}/wallet/paged?page=${page}&size=${size}&sort=asc`)
        .pipe(
            retry(1),
            catchError(err => this.error.errorHandler(err))
        );
  }

  SaveWallet(walletData: string): Observable<WalletResponseModel>{
    return this.http.post<WalletResponseModel>(`${this.baseurl}/wallet/create`,walletData,this.httpOptions)
        .pipe(
            retry(1),
            catchError(err => this.error.errorHandler(err))
        )
  }
}
