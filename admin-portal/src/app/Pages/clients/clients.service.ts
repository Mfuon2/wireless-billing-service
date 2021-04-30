import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError, retry} from 'rxjs/operators';
import {ClientsResponseDto} from '../../Models/Clients/clients';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ClientsService {

  baseurl = environment.baseurl;
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  constructor(private http: HttpClient) {
  }

  GetPagedClients(): Observable<ClientsResponseDto> {
    return this.http.get<ClientsResponseDto>(this.baseurl + '/account/paged?page=0&size=1000&sort=desc')
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
