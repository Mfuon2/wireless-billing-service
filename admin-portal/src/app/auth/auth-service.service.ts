import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import * as md5 from 'md5'
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private router:Router) { }

  isAuthenticated() : boolean {
    const username : string = 'nick';
    const password : string = '!nickson@Admin123';
    if(md5(username) === localStorage.getItem('username') && md5(password) === localStorage.getItem('password')) {
      const token = localStorage.getItem('token');
      return token === (md5(password) + md5(username));
    }
  }
}
