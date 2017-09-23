
const enum LifeCycle {
    'ACTIVE',
    'INACTIVE',
    'DEPRECATED',
    'TERMINATED'

};
import { Task } from '../task';
import { JUser } from '../j-user';
export class Attachment {
    constructor(
        public id?: number,
        public createdDate?: any,
        public modifyDate?: any,
        public lifeCycle?: LifeCycle,
        public content?: any,
        public name?: string,
        public task?: Task,
        public author?: JUser,
    ) { }
}
