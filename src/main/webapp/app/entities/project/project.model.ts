
const enum LifeCycle {
    'ACTIVE',
    'INACTIVE',
    'DEPRECATED',
    'TERMINATED'

};
import { JGroup } from '../j-group';
export class Project {
    constructor(
        public id?: number,
        public name?: string,
        public description?: any,
        public createdDate?: any,
        public modifyDate?: any,
        public lifeCycle?: LifeCycle,
        public jgroups?: JGroup[],
    ) { }
}
