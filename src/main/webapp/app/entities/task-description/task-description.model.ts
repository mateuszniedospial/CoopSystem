import { Task } from '../task';
export class TaskDescription {
    constructor(
        public id?: number,
        public createdDate?: any,
        public modifyDate?: any,
        public content?: any,
        public task?: Task,
    ) { }
}
