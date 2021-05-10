import {Component} from '@angular/core';
import {select} from '@angular-redux/store';
import {Observable} from 'rxjs';
import {ConfigActions} from '../../ThemeOptions/store/config.actions';
import {ThemeOptions} from '../../theme-options';
import {animate, query, style, transition, trigger} from '@angular/animations';

@Component({
  selector: 'app-base-layout',
  templateUrl: './base-layout.component.html',
})

export class BaseLayoutComponent {

  @select('config') public config$: Observable<any>;

  constructor(public globals: ThemeOptions, public configActions: ConfigActions) {
  }

  toggleSidebarMobile() {
    this.globals.toggleSidebarMobile = !this.globals.toggleSidebarMobile;
  }
}



