/**
 * Created by Chrono on 21.04.2017.
 */
import {Component, OnInit, OnDestroy} from "@angular/core";
import {JUser} from "../../entities/j-user/j-user.model";
import {JGroup} from "../../entities/j-group/j-group.model";
import {Subscription} from "rxjs";
import {JUserService} from "../../entities/j-user/j-user.service";
import {Principal} from "../auth/principal.service";
import {JGroupService} from "../../entities/j-group/j-group.service";
import {EventManager, AlertService} from "ng-jhipster";
import {Response} from "@angular/http";
import * as globals from "../../globals/globals";

@Component({
    selector: 'jgroup-storage',
    templateUrl: 'jgroup-storage.component.html',
    styleUrls: [
    ]
})
export class JGroupStorageComponent implements OnInit, OnDestroy{
    private jUser: JUser;
    private jGroups: JGroup[];
    private jGroupsWithoutSelected: JGroup[];

    selectedJGroup: JGroup;

    eventSubscriber: Subscription;

    constructor(private principal: Principal,
                private alertService: AlertService,
                private jUserService: JUserService,
                private jGroupService: JGroupService,
                private eventManager: EventManager){

    }

    ngOnInit(){
        this.initializeDataGathering();
        this.registerEvents();
    }

    private registerEvents(){
        this.registerChangeInJGroups();
    }

    initializeDataGathering(){
        this.principal.identity().then((user) => {
            this.jUserService.queryJUser(user.id).subscribe(jUser => {
                this.jUser = jUser;
                this.queryJGroupsData(jUser.id);
            });
        });
    }

    queryJGroupsData(id) {
        this.jGroupService.querySpecificUserGroups(id).subscribe((res: Response) => {
                this.jGroups = res.json();
                if(!JGroupStorageComponent.retrieveActiveJGroupID()) {
                    this.storeActiveJGroupID(this.jGroups[0]);
                } else {
                    if(this.jGroupStillExists()){
                        this.collectSelectedJGroup();
                    } else {
                        this.storeActiveJGroupID(this.jGroups[0]);
                    }
                }
            },
            (res: Response) => this.onError(res.json())
        );
    }

    jGroupStillExists(){
        for(let jGroup of this.jGroups){
            if(jGroup.id === Number(JGroupStorageComponent.retrieveActiveJGroupID())){
                return true;
            }
        }
        return false;
    }

    private collectSelectedJGroup(){
        this.jGroupService.find(Number(JGroupStorageComponent.retrieveActiveJGroupID())).subscribe(jGroup => {
            this.selectedJGroup = jGroup;
        });
    }

    private fillJGroupsWithoutSelected(){
        this.jGroupsWithoutSelected = [];
        for(let jGroup of this.jGroups){
            if(!(jGroup.id === Number(JGroupStorageComponent.retrieveActiveJGroupID()))){
                this.jGroupsWithoutSelected.push(jGroup);
            }
        }
    }

    private initJGroupsReload(){
        this.principal.identity().then((user) => {
            this.jUserService.queryJUser(user.id).subscribe(jUser => {
                this.queryJGroupsData(jUser.id);
            });
        });
    }

    private storeActiveJGroupID(jGroup: JGroup){
        this.selectedJGroup = jGroup;
        localStorage.setItem(globals.activeJGroupID, JSON.stringify(jGroup.id));
        this.broadcastActiveIDChangeEvent();
    }

    triggerStoreJGroupID(obj: JGroup){
        this.storeActiveJGroupID(obj);
    }

    private broadcastActiveIDChangeEvent(){
        this.eventManager.broadcast({name: 'activeJGroupIDModification', content: 'OK'});
    }

    public static retrieveActiveJGroupID(){
        let activeJGroupID:string = localStorage.getItem(globals.activeJGroupID);
        if(!activeJGroupID){
            return null;
        }
        return activeJGroupID;
    }

    registerChangeInJGroups(){
        this.eventSubscriber = this.eventManager.subscribe('jGroupListModification', (response) => {
            this.initJGroupsReload()
        });
    }

    registerChangeInActiveJGroupID(){
        this.eventSubscriber = this.eventManager.subscribe('activeJGroupIDModification', (response) => {
            this.jGroupService.find(Number(JGroupStorageComponent.retrieveActiveJGroupID())).subscribe(jGroup => {
                this.selectedJGroup = jGroup;
            });
        });
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }
}
