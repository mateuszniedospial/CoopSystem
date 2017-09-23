import {Project} from "../project/project.model";
export const enum TaskLifeCycle {
    'TODO',
    'INPROGRESS',
    'DEVDONE',
    'REVIEWED',
    'TEST',
    'DONE',
    'CLOSED'

}
;

export const enum TaskType{
    'TTASK',
    'BUG',
    'NABUG',
    'STORY',
    'EPIC',
    'DOCUTASK',
    'ADMINTASK',
    'ADHOC'
}
;

export const enum TaskPriority {
    'MAJOR',
    'MINOR',
    'BLOCKER',
    'TRIVIAL'

}
;
import {Sprint} from '../sprint';
import {JGroup} from '../j-group';
import {JUser} from '../j-user';
export class Task {
    static taskTypeItem = ['Technical task', 'Bug','Not a bug', 'Story', 'Epic', 'Documentation','Administration','Ad hoc'];
    static taskPriorityItem = ['Major','Minor','Trivial', 'Blocker'];
    static taskStatusItem = ['To do','In Progress','Development Done','In test', 'Reviewed', 'Done', 'Closed'];
    static taskTypeStringMap : { [key:string]:string; } = {'TTASK':'Technical task','BUG':'Bug','NABUG':'Not a bug', 'STORY':'Story', 'EPIC':'Epic',  'DOCUTASK':'Documentation','ADMINTASK':'Administration', 'ADHOC':'Ad hoc'};
    static taskPriorityStringMap : { [key:string]:string; } = {'MAJOR':'Major','MINOR':'Minor','TRIVIAL':'Trivial', 'BLOCKER':'Blocker'};
    static taskStatusStringMap : { [key:string]:string; } = {'TODO':'To do','INPROGRESS':'In Progress','DEVDONE':'Development Done','REVIEWED': 'Reviewed','TEST':'In test','DONE':'Done', 'CLOSED':'Closed'};
    static taskFieldString = ['lifeCycle', 'enviroment','title', 'type','priority','version','estimateTime', 'remainingTime','sprint', 'assignee','watchers','children'];
    constructor(public id?: number,
                public createdDate?: any,
                public modifyDate?: any,
                public lifeCycle?: TaskLifeCycle,
                public enviroment?: string,
                public title?: string,
                public type?: TaskType,
                public priority?: TaskPriority,
                public version?: string,
                public remainingTime?: string,
                public estimateTime?: string,
                public sprint?: Sprint,
                public parent?: Task,
                public jGroup?: JGroup,
                public jgroup?: JGroup,
                public assignee?: JUser,
                public reporter?: JUser,
                public watchers?: JUser[],
                public task?: Task,
                public child?: Task,
                public juser?: JUser,//using to indicate modified user
                public historic_sprints?: Sprint[],
                public project?: Project,) {
    }
    public static translateTaskLifeCycleToString(lifeCycle: TaskLifeCycle) {
        return Task.taskStatusStringMap[lifeCycle];
    }

    public static translateTaskPriorityToString(prio: TaskPriority) {
        return Task.taskPriorityStringMap[prio];
    }

    public static translateTaskTypeToString(type: TaskType) {
        return Task.taskTypeStringMap[type];
    }

    public static translateStringToType(input:any) :any {
        return Task.translateStringTo(input, Task.taskTypeStringMap);
    }
    public static translateStringToPriority(input:string) :any {
        return Task.translateStringTo(input, Task.taskPriorityStringMap);
    }

    public static translateStringToLifeCycle(input:any) :any {
        return Task.translateStringTo(input, Task.taskStatusStringMap);
    }

    private static translateStringTo(input: string, internalMap: any) {
        for (let key in  internalMap) {
            if (internalMap[key] == input) {
                return key;
            }
        }
    }


}
