import { Task } from '../task';
import {JUser} from "../j-user/j-user.model";
export class Comment {
    constructor(
        public id?: number,
        public createdDate?: any,
        public modifyDate?: any,
        public content?: any,
        public task?: Task,
        public author?:JUser
    ) { }

}
