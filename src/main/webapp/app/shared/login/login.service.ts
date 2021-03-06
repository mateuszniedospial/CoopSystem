import { Injectable } from '@angular/core';

import { Principal } from '../auth/principal.service';
import { AuthServerProvider } from '../auth/auth-session.service';
import * as globals from "../../globals/globals";

@Injectable()
export class LoginService {

    constructor (
        private principal: Principal,
        private authServerProvider: AuthServerProvider
    ) {}

    login (credentials, callback?) {
        let cb = callback || function() {};

        return new Promise((resolve, reject) => {
            this.authServerProvider.login(credentials).subscribe(data => {
                this.principal.identity(true).then(account => {
                    resolve(data);
                });
                return cb();
            }, err => {
                this.logout();
                reject(err);
                return cb(err);
            });
        });
    }

    logout () {
        this.authServerProvider.logout().subscribe();
        this.destroyLocalStorageActiveJGroupID();
        this.principal.authenticate(null);
    }

    private destroyLocalStorageActiveJGroupID(){
        localStorage.removeItem(globals.activeJGroupID);
    }
}
