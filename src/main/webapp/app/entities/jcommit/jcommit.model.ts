import { JUser } from '../j-user';
import { Task } from '../task';
export class Jcommit {
    constructor(
        public id?: number,
        public message?: string,
        public modifiedList?: string[],
        public removedList?: string[],
        public addedList?: string[],
        public commitDate?: any,
        public url?: string,
        public juser?: JUser,
        public tasks?: Task,
    ) { }
}
