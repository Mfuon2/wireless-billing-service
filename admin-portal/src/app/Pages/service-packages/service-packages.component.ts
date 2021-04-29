import { Component, OnInit } from '@angular/core';
import {ServicePackagesService} from './service-packages.service';
import {LocalDataSource} from 'ng2-smart-table';

@Component({
  selector: 'app-service-packages',
  templateUrl: './service-packages.component.html',
  styleUrls: ['./service-packages.component.sass']
})
export class ServicePackagesComponent implements OnInit {
  heading = 'Service Packages';
  subheading = 'Lists all Service Packages';
  icon = 'pe-7s-network icon-gradient bg-tempting-azure';
  private source: any;
  settings = {
    actions: {
      position: 'right',
      columnTitle: 'ACTIONS',
      custom: [
        {
          name: 'add',
          title: '<i class="pe-7s-album text-white opacity-8">Add</i>'
        },
        {
          name: 'editAction',
          title: '<i class="ion-edit" title="Edit"></i>'
        },
        {
          name: 'deleteAction',
          title: '<i class="far fa-trash-alt" title="delete"></i>'
        }
      ]
    },
    edit: {
      createButtonContent: {
        title: 'Edit',
        content: '<i class="pe-7s-album btn btn-outline-success text-white opacity-8">Add</i>'
      }
    },
    attr: {
      class: 'table table-bordered table-striped table-sm'
    },
    pager: {
      display: true,
      perPage: 12
    },
    columns: {
      id: {title: 'ID'},
      name: {title: 'Name'},
      code: {title: 'Code'},
      description: {title: 'Description'},
      price: {title: 'price'},
      cycle: {title: 'Cycle'},
      createdAt: {title: 'Date Created'}
    }
  };
  data: any;

  constructor(private packageService: ServicePackagesService) { }

  ngOnInit(): void {
    this.loadClients();
    this.source = new LocalDataSource(this.data);
  }

  onSearch(query: string = '') {
    this.source.setFilter([
      {
        field: 'name',
        search: query
      },
      {
        field: 'code',
        search: query
      },
      {
        field: 'price',
        search: query
      },
      {
        field: 'description',
        search: query
      }
    ], false);

  }
  // Get services list
  loadClients() {
    return this.packageService.GetPagedServices().subscribe((data) => {
      this.data = data.content;
    });
  }
}
