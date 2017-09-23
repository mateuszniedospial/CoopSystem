import { Task } from '../task';

/**
 * Created by Master on 29.04.2017.
 */
export class ReportContent {
    constructor(
        public  estimateStart? : number,
        public  remainingStart? : number,
        public  sumOfWorklog? : number,
        public  sumOfEstimate? : number,
        public  sumOfRemaining? : number,
        public  workLogPerUser? : any[],
        public  tasksIdDone? : number[],
        public  tasksIdUnDone? : number[],
        public  tasksIdAddedInSprint? : number[],
        public  tasksDone? : Task[],
        public  tasksUnDone? : Task[],
        public  tasksAddedInSprint? : Task[],
        public  plotEntitiesRemaning? : PlotEntity[],
        public  nextReportId?: number,
        public  previousReportId?: number) { }
}

export class PlotEntity {
    public time?:Date;
    public value?:any;
}
