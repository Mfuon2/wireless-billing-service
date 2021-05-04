import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {catchError, retry} from 'rxjs/operators';
import {ClientCreationModel, ClientsResponseDto} from '../../Models/Clients/clients';
import {environment} from '../../../environments/environment';
import {HttpErrorHandler} from '../../HttpErrorHandler';

@Injectable({
  providedIn: 'root'
})
export class ClientsService {

  baseurl = environment.baseurl;
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin':'*'
    })
  };

  constructor(private http: HttpClient, private error : HttpErrorHandler) {
  }

  GetPagedClients(page: number, size: number): Observable<ClientsResponseDto> {
    return this.http.get<ClientsResponseDto>(`${this.baseurl}/account/paged?page=${page}&size=${size}&sort=asc`)
        .pipe(
            retry(1),
            catchError(err => this.error.errorHandler(err))
        );
  }

  GetClientAccount(clientAccount: string): Observable<ClientCreationModel> {
    return this.http.get<ClientCreationModel>(`${this.baseurl}/account/get?accountNumber=${clientAccount}`)
        .pipe(
            retry(1),
            catchError(err => this.error.errorHandler(err))
        );
  }

  SaveClientsAccount(clientData: string): Observable<ClientCreationModel>{
    return this.http.post<ClientCreationModel>(`${this.baseurl}/account/create`,clientData,this.httpOptions)
        .pipe(
            retry(1),
            catchError(err => this.error.errorHandler(err))
        )
  }
}
