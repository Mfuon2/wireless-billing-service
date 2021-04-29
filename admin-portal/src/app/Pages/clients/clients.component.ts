import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {LocalDataSource} from 'ng2-smart-table';
import {ClientsService} from './clients.service';

@Component({
    selector: 'app-clients',
    templateUrl: './clients.component.html',
    encapsulation: ViewEncapsulation.None
})
export class ClientsComponent implements OnInit {


    heading = 'Clients Accounts';
    subheading = 'Lists all Clients Accounts';
    icon = 'pe-7s-users icon-gradient bg-tempting-azure';


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
          accountNumber: {title: 'Account No'},
          accountName: {title: 'Account Name'},
          shortCode: {title: 'Code'},
          emailAddress: {title: 'Email'},
          serviceType: {title: 'Service Type'},
          createdAt: {title: 'Date Created'}
        }
    };

    data = [];

    private source: LocalDataSource;

    constructor(private clientService: ClientsService) {
    }

    ngOnInit(): void {
        this.loadClients();
        this.source = new LocalDataSource(this.data);
    }

    onSearch(query: string = '') {
        this.source.setFilter([
            {
                field: 'accountNumber',
                search: query
            },
            {
                field: 'accountName',
                search: query
            },
            {
                field: 'emailAddress',
                search: query
            },
            {
                field: 'serviceType',
                search: query
            }
        ], false);

    }

    // Get clients list
    loadClients() {
        return this.clientService.GetPagedClients().subscribe((data) => {
            this.data = data.content;
        });
    }

}
