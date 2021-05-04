import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs';
import {catchError, retry} from 'rxjs/operators';
import {ServicePackageResponseModel, ServicesResponseDto} from '../../Models/Packages/service_package';
import {HttpErrorHandler} from '../../HttpErrorHandler';
import {ClientCreationModel} from '../../Models/Clients/clients';

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
  constructor(private http: HttpClient, private error : HttpErrorHandler) { }

  GetPagedServices(): Observable<ServicesResponseDto> {
    return this.http.get<ServicesResponseDto>(this.baseurl + '/package/paged?page=0&size=1000&sort=desc')
        .pipe(retry(1),
            catchError((err) => this.error.errorHandler(err))
        );
  }

  SaveServicePackage(servicePackageData: string): Observable<ServicePackageResponseModel>{
    return this.http.post<ServicePackageResponseModel>(`${this.baseurl}/package/create`,servicePackageData,this.httpOptions)
        .pipe(
            retry(1),
            catchError(err => this.error.errorHandler(err))
        )
  }

  GetServicePackage(searchQuery: string): Observable<ServicePackageResponseModel> {
    return this.http.get<ServicePackageResponseModel>(`${this.baseurl}/package/get?code=${searchQuery}`)
        .pipe(
            retry(1),
            catchError(err => this.error.errorHandler(err))
        );
  }
}
