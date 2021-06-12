
import {Injectable} from '@angular/core';
import * as md from 'md5';

@Injectable({
    providedIn: 'root'
})
export class LoggerService {
    constructor() {
    }

    temUser() {
        const password = '!nickson@Admin123';
        const username = 'nick';
        localStorage.setItem('username',md(username))
        localStorage.setItem('password',md(password))
        localStorage.setItem('token',md(password)+md(username))
    }
}
