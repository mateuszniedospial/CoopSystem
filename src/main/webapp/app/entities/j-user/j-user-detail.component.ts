import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JUser } from './j-user.model';
import { JUserService } from './j-user.service';

@Component({
    selector: 'jhi-j-user-detail',
    templateUrl: './j-user-detail.component.html'
})
export class JUserDetailComponent implements OnInit, OnDestroy {

    jUser: JUser;
    private subscription: any;

    constructor(
        private jUserService: JUserService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.jUserService.find(id).subscribe(jUser => {
            this.jUser = jUser;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
