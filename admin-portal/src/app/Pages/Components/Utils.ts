import {Injectable} from '@angular/core';
import {ConfirmDialog} from 'primeng/confirmdialog';
import {ConfirmationService, MessageService} from 'primeng/api';

@Injectable({
    providedIn: 'root'
})

export class Utils{
    constructor(private confirmationService: ConfirmationService, private messageService: MessageService) {
    }

    confirm(msg: string) {
        this.confirmationService.confirm({
            message: `${msg}`,
            accept: () => {
            }
        });
    }

    showSuccess(msg: string){
        this.messageService.add({severity:'success', summary: 'Successful', detail: `${msg}`, life: 3000});
    }
    showError(msg: string) {
        this.messageService.add({severity:'error', summary: 'Error', detail: `${msg}`, life: 3000});
    }
}