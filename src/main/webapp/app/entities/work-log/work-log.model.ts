import { JUser } from '../j-user';
import { Task } from '../task';
export class WorkLog {
    constructor(
        public id?: number,
        public createdDate?: any,
        public modifyDate?: any,
        public startWork?: any,
        public stopWork?: any,
        public description?: string,
        public timeInHour?: number,
        public juser?: JUser,
        public task?: Task,
    ) { }
}
