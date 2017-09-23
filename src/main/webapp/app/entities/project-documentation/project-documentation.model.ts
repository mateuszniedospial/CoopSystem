
const enum LifeCycle {
    'ACTIVE',
    'INACTIVE',
    'DEPRECATED',
    'TERMINATED'

};
import { JUser } from '../j-user';
import { Project } from '../project';
import { DocumentationCatalogue } from '../documentation-catalogue';
export class ProjectDocumentation {
    constructor(
        public id?: number,
        public createdDate?: any,
        public modifyDate?: any,
        public version?: number,
        public content?: any,
        public lifeCycle?: LifeCycle,
        public label?: string,
        public data?: string,
        public lastAuthor?: JUser,
        public project?: Project,
        public underCatalogue?: DocumentationCatalogue,
    ) { }
}
