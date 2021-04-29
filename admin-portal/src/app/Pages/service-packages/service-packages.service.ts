import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {Observable, throwError} from 'rxjs';
import {ClientsResponseDto} from '../../Models/Clients/clients';
import {catchError, retry} from 'rxjs/operators';
import {ServicesResponseDto} from '../../Models/Packages/service_package';

@Injectable({
  providedIn: 'root'
})
export class ServicePackagesService {

  baseurl = environment.baseurl;
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };
  constructor(private httpClient: HttpClient) { }

  GetPagedServices(): Observable<ServicesResponseDto> {
    return this.httpClient.get<ServicesResponseDto>(this.baseurl + '/package/paged?page=0&size=1000&sort=desc')
        .pipe(retry(1),
            catchError(this.errorHandler)
        );
  }

  errorHandler(error) {
    let errorMessage = '';
    if (error.error instanceof ErrorEvent) {
      errorMessage = error.error.message;
    } else {
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    console.log(errorMessage);
    return throwError(errorMessage);
  }
}
