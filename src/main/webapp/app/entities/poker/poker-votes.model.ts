import { JUser } from '../j-user';
import { Poker } from '../poker';
export class PokerVotes {
    constructor(
        public id?: number,
        public vote?: number,
        public juser?: JUser,
        public poker?: Poker,
    ) { }
}
