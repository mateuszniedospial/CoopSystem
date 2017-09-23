import { Sprint } from '../sprint';
import { JGroup } from '../j-group';
import { Task } from '../task';
import {PokerVotes} from "./poker-votes.model";
export class Poker {
    constructor(
        public id?: number,
        public isStarted?: boolean,
        public isStoped?: boolean,
        public title?: string,
        public sprint?: Sprint,
        public jGroup?: JGroup,
        public pokerVotes?: PokerVotes,
        public task?: Task,
    ) { }
}
