import {Component, OnInit, OnDestroy} from "@angular/core";
import {Task} from "../entities/task/task.model";
import {Principal} from "../shared/auth/principal.service";
import {SearchService} from "./search.service";
import {JGroupStorageComponent} from "../shared/groupLocalStorage/jgroup-storage.component";
import {Response} from "@angular/http";
import {JGroupService} from "../entities/j-group/j-group.service";
import {JGroup} from "../entities/j-group/j-group.model";
import {DateUtil} from "../shared/util/date-util";
import {TaskIconUtil} from "../shared/util/task-icon-util";
import {Subscription} from "rxjs";
import {EventManager} from "ng-jhipster";
import {Project} from "../entities/project/project.model";
import {ProjectService} from "../entities/project/project.service";

@Component({
    selector: 'search-comp',
    templateUrl: './search.component.html',
    styleUrls: ["search.css"]

})
export class SearchComponent implements OnInit, OnDestroy {

    /**Static reference using in view .html */
    public taskIconStaticRef: TaskIconUtil = TaskIconUtil;
    public dateUtilStaticRef: DateUtil = DateUtil;
    public taskStaticRef: Task = Task;
    /**end of static*/

    taskStatusItem: any[] = Task.taskStatusItem;
    taskPriorityItem: any[] = Task.taskPriorityItem;
    result: Task[] = [];
    projects: Project[] = [];
    progress: number = 0;
    customCheckBox: CustomCheckbox;
    queryParameter: QueryParameter;

    private idOfSelectedGroup: string;
    private jGroupWithUsers: JGroup;

    private eventSubscriber: Subscription;

    constructor(private principal: Principal, private searchService: SearchService, private jGroupService: JGroupService, private eventManager: EventManager,
                private projectService: ProjectService) {
    };

    ngOnInit(): void {
        this.customCheckBox = new CustomCheckbox();
        this.queryParameter = new QueryParameter();
        this.principal.identity().then(() => {
            this.initBaseData();
            this.initProjects();
        });
        this.registerChangeGroupListener()

    }

    ngOnDestroy(): void {
        this.eventSubscriber.unsubscribe();
    }

    private initBaseData() {
        this.idOfSelectedGroup = JGroupStorageComponent.retrieveActiveJGroupID();
        this.queryParameter.jGroupId = this.idOfSelectedGroup;
        this.initJGroup();
    }

    private initJGroup() {
        this.jGroupService.find(parseInt(this.idOfSelectedGroup))
            .subscribe(
                (res: Response) => {
                    this.jGroupWithUsers = res;
                },
                () => {
                    this.onError()
                });
    }

    public searchClick() {
        this.progress = 1;
        let qP: QueryParameter = Object.assign({}, this.queryParameter);
        qP = this.convertQueryParamter(qP);
        this.searchService.query(qP, this.customCheckBox)
            .subscribe((res: Response) => {
                    let response: any[];
                    response = res.json();
                    this.result = response;
                    this.progress = 100;
                },
                () => {
                    this.onError();
                });
    }


    protected onError(any?: any) {
        console.log("Application error in script")
    }

    private convertQueryParamter(qP: QueryParameter): QueryParameter {
        qP.priority = Task.translateStringToPriority(qP.priority);
        qP.status = Task.translateStringToLifeCycle(qP.status);
        return qP
    }

    private registerChangeGroupListener() {
        this.eventSubscriber = this.eventManager.subscribe('activeJGroupIDModification', () => {
            this.initBaseData();
        });
    }

    private initProjects() {
        let gId : any = this.idOfSelectedGroup;
        this.projectService.queryByGroupId(gId)
            .subscribe(res => {
            for (let project of res.json()) {
                this.projects.push(project);
            }
        })

    }
}

export class CustomCheckbox {
    isTitle: boolean = false;
    isId: boolean = false;
    isVersion: boolean = false;
    isEnviroment: boolean = false;
    isStatus: boolean = false;
    isAssigne: boolean = false;
    isComment: boolean = false;
    isProject: boolean = false;
    isDescription: boolean = false;
    isPrioritu: boolean = false;
    isAfterCreated: boolean = false;
    isBeforeCreated: boolean = false;
    isAfterModfied: boolean = false;
    isBeforeModified: boolean = false;
}

export class QueryParameter {
    title: string;
    id: string;
    version: string;
    enviroment: string;
    status: string;
    assignee: any;
    comment: string;
    project: Project;
    description: string;
    priority: string;
    createDate: Date;
    modifiedDate: Date;
    jGroupId: any;
}
