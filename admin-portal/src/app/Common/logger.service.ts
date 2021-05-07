
import {Injectable} from '@angular/core';
import * as chalk from 'chalk';

@Injectable({
    providedIn: 'root'
})
export class LoggerService{
    constructor() {
    }
    info(msg: string){
        console.log(chalk.blue.italic(msg))
    }

    err(msg: string, e: string){
        console.log(chalk.red.italic(msg +' ----> ' + e))
    }
}
