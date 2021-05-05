import {Injectable} from '@angular/core';
import {ClientContent} from '../Models/Clients/clients';
import {ServicePackagesService} from '../Pages/service-packages/service-packages.service';
import {ClientsService} from '../Pages/clients/clients.service';
import {ServicePackageContent} from '../Models/Packages/service_package';

@Injectable({
    providedIn: 'root'
})
export class ServiceService {
    clientContents: ClientContent[];
    serviceContents: ServicePackageContent[];

    constructor(private packagesService: ServicePackagesService,
                private clientService: ClientsService,) {
    }

    async loadClientDataList(): Promise<ClientContent[]> {
        const sub = await this.clientService.GetPagedClients(0, 10000).toPromise();
        this.clientContents = sub.content;
        return this.clientContents;
    }

    async loadPackagesDataList(): Promise<ServicePackageContent[]> {
        const sub = await this.packagesService.GetPagedServices().toPromise();
        this.serviceContents = sub.content;
        return this.serviceContents;
    }

}
