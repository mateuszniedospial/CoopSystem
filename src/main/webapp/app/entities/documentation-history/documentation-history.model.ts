
const enum LifeCycle {
    'ACTIVE',
    'INACTIVE',
    'DEPRECATED',
    'TERMINATED'

};
import { ProjectDocumentation } from '../project-documentation';
import { JUser } from '../j-user';
export class DocumentationHistory {
    constructor(
        public id?: number,
        public createdDate?: any,
        public content?: any,
        public version?: number,
        public modifyDate?: any,
        public lifeCycle?: LifeCycle,
        public projectDocumentation?: ProjectDocumentation,
        public lastAuthor?: JUser,
    ) { }
}
