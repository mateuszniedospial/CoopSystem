
const enum LifeCycle {
    'ACTIVE',
    'INACTIVE',
    'DEPRECATED',
    'TERMINATED'

};
import { JUser } from '../j-user';
export class JGroup {
    constructor(
        public id?: number,
        public name?: string,
        public description?: any,
        public createdDate?: any,
        public modifyDate?: any,
        public lifeCycle?: LifeCycle,
        public juser?: JUser,
        public jusers?: JUser[],
    ) { }
}
