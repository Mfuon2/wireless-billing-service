import {throwError} from 'rxjs';
import {Injectable} from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class HttpErrorHandler {
    errorHandler(error:any) {
        let errorMessage = '';
        if(error.error.subErrors === null || error.error.subErrors === undefined){
            errorMessage = `Something wrong has occurred`
        } else if (error.status instanceof ErrorEvent) {
            errorMessage = `Field -> ${error.error.subErrors[0].field} Rejected -> ${error.error.subErrors[0].rejectedValue} Reason -> ${error.error.subErrors[0].message}`;
        } else {
            errorMessage = `Error Message:  ${error.error.subErrors[0].message}`;
        }
        return throwError(errorMessage);
    }
}
