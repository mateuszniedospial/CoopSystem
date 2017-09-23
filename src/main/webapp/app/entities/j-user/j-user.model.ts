import { Task } from '../task';
import {User} from "../../shared/user/user.model";
export class JUser {
    constructor(
        public id?: number,
        public user?: User,
        public task?: Task,
    ) { }
}
