import { JGroup } from '../j-group';
import { JUser } from '../j-user';
export class JoinJGroupRequest {
    constructor(
        public id?: number,
        public description?: string,
        public createdDate?: any,
        public jgroup?: JGroup,
        public whoRequested?: JUser,
    ) { }
}
