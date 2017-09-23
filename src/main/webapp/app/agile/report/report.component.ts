import {Component, OnInit, OnDestroy} from "@angular/core";
import {DragulaService} from "ng2-dragula";
import {AgileAbstractComponent} from "../base/agile-abstract.component";
import {Principal} from "../../shared/auth/principal.service";
import {JUserService} from "../../entities/j-user/j-user.service";
import {JUserImgService} from "../../entities/j-user-img/j-user-img.service";
import {JGroupService} from "../../entities/j-group/j-group.service";
import {TaskService} from "../../entities/task/task.service";
import {TaskDescriptionService} from "../../entities/task-description/task-description.service";
import {EventManager} from "ng-jhipster";
import {SprintService} from "../../entities/sprint/sprint.service";
import {Response} from "@angular/http";
import {Task} from '../../entities/task/';
import {ReportService} from "../../entities/report/report.service";
import {Report} from "../../entities/report/report.model";
import {PlotEntity} from "../../entities/report/report-content.model";
import {DateUtil} from "../../shared/util/date-util";
import {WorkLogService} from "../../entities/work-log/work-log.service";
import {WorkLog} from "../../entities/work-log/work-log.model";
import {Multimap, ArrayListMultimap} from "../../shared/util/multi-map";
import {ActivatedRoute} from "@angular/router";
import {Sprint} from "../../entities/sprint/sprint.model";
import {ScriptLazyLoader} from "../../shared/util/scritp-lazy-loader";

/**
 * Created by Master on 21.02.2017.
 */
@Component({
    selector: 'reportView',
    templateUrl: './report.component.html',
    styleUrls: [
        './report.css'
    ]
})
export class ReportComponent extends AgileAbstractComponent implements OnInit, OnDestroy {


    private report: Report;
    private workLogPerTaskMap: Multimap<Task, WorkLog> = ArrayListMultimap.create() ;
    private isReportComponent: boolean ;
    private subscription:any;
    private sprintId:number;

    constructor(protected dragulaService: DragulaService,
                protected principal: Principal,
                protected jUserService: JUserService,
                protected jUserImgService: JUserImgService,
                protected jGroupService: JGroupService,
                protected taskService: TaskService,
                protected taskDescriptionService: TaskDescriptionService,
                protected eventManager: EventManager,
                protected sprintService: SprintService,
                protected reportService: ReportService,
                protected worklogService: WorkLogService,
                private route: ActivatedRoute) {
        super(dragulaService, principal, jUserService, jUserImgService, jGroupService, taskService, taskDescriptionService, eventManager, sprintService);
    }

    ngOnInit(): void {
        this.subscription = this.route.params.subscribe(params => {
            ScriptLazyLoader.loadScript('/scripts/chart.js')
            this.sprintId = params['id'];

            if(this.sprintId == 0 ) {
                super.init('report');
                this.isReportComponent = true;
                this.registerChangeInSelectedJGroupID();
            } else {
                this.isReportComponent = true;
                this.principal.identity().then((account) => {
                    this.initJUserIDByJhiId(account.id);
                    this.initDemandReport();
                });
            }
        })
    }

    ngOnDestroy() {
        this.eventSubscribers.forEach(it=>{it.unsubscribe()})
    }

    /** This method is not implemented because is abstract **/
    initTasks() {}//unused
    removeAllTasksFromSprint() {}//unused
    destroyData() {}
    /** end of non implemented method **/

    nextSprintClick() {
        this.initNewReportIfClicked(this.report.reportContent.nextReportId);
    }

    private initNewReportIfClicked(id:number) {
        this.isReportComponent = true;
        this.reportService.find(id).subscribe(
            (report: Report) => {
                this.report = report;
                this.sprint = report.sprint;
                this.createView();
            },
            () => {this.onError()}
        )
    }

    previousSprintClick() {
        this.initNewReportIfClicked(this.report.reportContent.previousReportId);
    }

    registerChangeInSelectedJGroupID(){
        this.eventSubscribers.push(this.eventManager.subscribe('activeJGroupIDModification', (response) => this.deleteReportData()));
    }

     createReportView() {
        if(this.isReportComponent) {
            this.reportService.queryBySprintId(this.sprint.id).subscribe(
                (res: Response) => {
                    this.report = res.json();
                    if(this.sprintId != 0) {
                        this.createViewForDemandReport();
                    } else {
                        this.createView();
                    }
                    },
                () => {this.onError();});
        }
    }

    private createViewForDemandReport() {
        this.sprint = this.report.sprint;
        this.jGroup = this.report.sprint.jgroup;
        this.jGroupService.find(this.jGroup.id).subscribe(
            (res: Response) => {
                this.jGroup = res;
                this.createView();
            },
            (res: Response) => {
                this.onError(res)
            })
    }

    private createView() {
        this.initAllWorkLogs();
        this.clearData();
        this.lineChartLabels.push(DateUtil.formatToShortDate(this.sprint.startTime));
        this.createIdealRemaingFunction();
        this.createRealRemaningFunctionInPlot();
        this.createBarPlotDate();
        this.isReportComponent = false;
    }

    private createRealRemaningFunctionInPlot() {
        let plotData: PlotEntity[] = this.report.reportContent.plotEntitiesRemaning;
        if(plotData){
            let array: number[] = [];
            plotData.forEach(entity => {
                array.push(entity.value)
            });
            this.lineChartData.push({data: array, label: 'Real function'});
            this.lineChartLabels.push(DateUtil.formatToShortDate(this.sprint.stopTime));
        }

    }

    private createIdealRemaingFunction() {
        let remainingStart = this.report.reportContent.remainingStart;
        let startSprintDate : Date = new Date(this.sprint.startTime);
        let stopSprintDate : Date = new Date(this.sprint.stopTime);
        let localDate : Date =startSprintDate;
        let date : number = (startSprintDate.getDate()+1);
        localDate.setDate(date);
        let i =  1;
        let k = 1;
        let weekDayIndex: number[] = [];
        while(localDate.getTime() < stopSprintDate.getTime()){
            this.lineChartLabels.push(DateUtil.formatToShortDate(localDate.toString()));
            if(localDate.getDay()==6 || localDate.getDay()==0 ) {
                weekDayIndex.push(i+1);
                k--;
            }
            let date:number = (startSprintDate.getDate()+1);
            localDate.setDate(date);
            i++;
            k++;
        }
        i++;
        this.createValueForRemainingPlot(i, remainingStart, weekDayIndex, k);
    }

    private createValueForRemainingPlot(stepCounter: number, remainingStart: number, weekDayIndex: number[], k: number) {
        let values: number[] = [];
        values.push(remainingStart);
        for (let i = 1; i < stepCounter; i++) {
            let lastValue = values[values.length - 1];
            if (weekDayIndex.find(it => it == i)) {
                values.push(lastValue);
            } else {
                values.push((lastValue - remainingStart / k));
            }
        }
        values.push(0);
        this.lineChartData.push({data: values, label: 'Ideal remaining time function'})
    }

    private initWorkLogs(task:Task) {
        this.worklogService.queryByTaskId({'query': task.id})
            .subscribe(
                (res:Response)=>{
                    let workLogs : WorkLog[] = res.json();
                    workLogs.forEach(workLog=>{
                        this.workLogPerTaskMap.put(task, workLog);
                    })
                },
                ()=>{this.onError();})
    }

    private createBarPlotDate() {
        if(this.report.reportContent.workLogPerUser){
            if( this.report.reportContent.workLogPerUser.length>0){
                let data : number[]=[];
                this.jGroup.jusers.forEach(member=>{
                    this.barChartLabels.push(member.user.login);
                    let keyValue = this.report.reportContent.workLogPerUser.find(it=>it.key == member.id);
                    data.push(keyValue.value);
                });
                data.push(0);
                this.barChartData.push({data: data, label: '[hour]'});
            }
        }
    }

    getSumOfWorkLog(task :Task){
        let sum = 0;
        this.workLogPerTaskMap.get(task).forEach((it=>{
            sum+= it.timeInHour;
        }));
        return sum;
    }

    getSumOfWorkLogPerList(taskList: Task[]) {
        let sum = 0 ;
        if(taskList)
            taskList.forEach(task => {
                sum += this.getSumOfWorkLog(task);
            });
        return sum;
    }

    private initAllWorkLogs() {
        this.report.reportContent.tasksAddedInSprint.forEach(task => {this.initWorkLogs(task)});
        this.report.reportContent.tasksDone.forEach(task => {this.initWorkLogs(task)});
        this.report.reportContent.tasksUnDone.forEach(task => {this.initWorkLogs(task)});
    }

    private clearData() {
        this.barChartLabels=[];
        this.barChartData = [];
        this.lineChartData=[];
        this.lineChartLabels=[];
    }

    private deleteReportData(){
        this.report = null;
        this.workLogPerTaskMap = ArrayListMultimap.create();
        this.isReportComponent = true;
    }

    private initDemandReport() {
        this.sprint = new Sprint();
        this.sprint.id = this.sprintId;
        this.createReportView();
    }

    /**Chart setting*/
    // lineChart
    public lineChartData: Array<any> = [];
    public lineChartLabels: Array<any> = [];
    public lineChartOptions: any = {
        responsive: true
    };
    public lineChartColors: Array<any> = [
        { // grey
            backgroundColor: 'rgba(148,159,177,0.2)',
            borderColor: 'rgba(148,159,177,1)',
            pointBackgroundColor: 'rgba(148,159,177,1)',
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: 'rgba(148,159,177,0.8)'
        },
        { // dark grey
            backgroundColor: 'rgba(77,83,96,0.2)',
            borderColor: 'rgba(77,83,96,1)',
            pointBackgroundColor: 'rgba(77,83,96,1)',
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: 'rgba(77,83,96,1)'
        },
    ];
    public lineChartLegend: boolean = true;
    public lineChartType: string = 'line';

    public  chartClicked(e: any): void {
        console.log(e);
    }

    public  chartHovered(e: any): void {
        console.log(e);
    }

    /**Bar chart**/
    public barChartOptions:any = {
        scaleShowVerticalLines: false,
        responsive: true
    };
    public barChartLabels:string[] = [];
    public barChartType:string = 'bar';
    public barChartLegend:boolean = true;

    public barChartData:any[] = [];

    /**End of chart settings**/

}


