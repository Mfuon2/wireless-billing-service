import {Component, OnInit} from '@angular/core';
import {ThemeOptions} from '../../../../../theme-options';
import {Router} from '@angular/router';

@Component({
  selector: 'app-user-box',
  templateUrl: './user-box.component.html',
})
export class UserBoxComponent implements OnInit {

  constructor(public globals: ThemeOptions, private router:Router) {
  }

  ngOnInit() {
  }

  logout(){
    localStorage.setItem('password','-');
    localStorage.setItem('username','-');
    localStorage.setItem('token','-');
    localStorage.clear();
    this.router.navigate(['/login']).finally(() => {
    })
  }

}
