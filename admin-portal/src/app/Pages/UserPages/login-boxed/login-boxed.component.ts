import { Component, OnInit } from '@angular/core';
import * as md from 'md5'
import {LoggerService} from '../../../Common/logger.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login-boxed',
  templateUrl: './login-boxed.component.html',
})
export class LoginBoxedComponent implements OnInit {

  password: string;
  username: string;
  login: boolean;
  errorMessage: string;
  loginError: boolean;

  constructor(private logg: LoggerService, private router: Router ) { }

  ngOnInit() {}

  authenticateUser(): boolean {
    this.logg.temUser()
    const username = this.username;
    const password = this.password;
    if(md(username) === localStorage.getItem('username') && md(password) === localStorage.getItem('password')){
      const token = localStorage.getItem('token');
      return token === (md(password) + md(username));
    }
  }

  Login(){
    this.login = true;
    if(this.authenticateUser()) {
      this.router.navigate(['/en']).finally(() => {})
    } else {
      this.loginError = true;
      this.errorMessage = 'Error logging in, Provide correct login credentials'
      this.router.navigate(['/login']).finally(() => {})
    }
  }
}
