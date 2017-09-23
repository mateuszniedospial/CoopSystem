import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DataUtils } from 'ng-jhipster';
import { JGroup } from './j-group.model';
import { JGroupService } from './j-group.service';

@Component({
    selector: 'jhi-j-group-detail',
    templateUrl: './j-group-detail.component.html'
})
export class JGroupDetailComponent implements OnInit, OnDestroy {

    jGroup: JGroup;
    private subscription: any;

    constructor(
        private dataUtils: DataUtils,
        private jGroupService: JGroupService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.jGroupService.find(id).subscribe(jGroup => {
            this.jGroup = jGroup;
        });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
