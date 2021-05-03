import { Injectable } from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {HttpErrorHandler} from '../../HttpErrorHandler';
import {Observable} from 'rxjs';
import {ServicePackageResponseModel, ServicesResponseDto} from '../../Models/Packages/service_package';
import {catchError, retry} from 'rxjs/operators';
import {SubscriptionResponseDto} from '../../Models/Subscriptions/subscription_list_model';
import {SubscriptionResponseModel} from '../../Models/Subscriptions/subscriptions';

@Injectable({
  providedIn: 'root'
})
export class SubscriptionService {

  baseurl = environment.baseurl;
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };
  constructor(private http: HttpClient, private error : HttpErrorHandler) { }

  GetPagedSubscriptions(): Observable<SubscriptionResponseDto> {
    return this.http.get<SubscriptionResponseDto>(this.baseurl + '/subscription/paged?page=0&size=1000&sort=desc')
        .pipe(retry(1),
            catchError((err) => this.error.errorHandler(err))
        );
  }

  SaveSubscription(subscriptionData: string): Observable<SubscriptionResponseModel>{
    return this.http.post<SubscriptionResponseModel>(`${this.baseurl}/subscription/create`,subscriptionData,this.httpOptions)
        .pipe(
            retry(1),
            catchError(err => this.error.errorHandler(err))
        )
  }
}
