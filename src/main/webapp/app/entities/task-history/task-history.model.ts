import { JUser } from '../j-user';
import { Task } from '../task';
export class TaskHistory {
    constructor(
        public id?: number,
        public created_date?: any,
        public content?: any,
        public changedName?: string,
        public oldContent?: any,
        public modifiedJUser?: JUser,
        public task?: Task,
    ) { }
}
