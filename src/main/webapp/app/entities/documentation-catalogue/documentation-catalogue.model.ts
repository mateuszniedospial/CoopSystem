import { Project } from '../project';
export class DocumentationCatalogue {
    constructor(
        public id?: number,
        public label?: string,
        public data?: string,
        public parent?: DocumentationCatalogue,
        public project?: Project,
    ) { }
}
