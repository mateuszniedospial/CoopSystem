import { Sprint } from '../sprint';
import {ReportContent} from "./report-content.model";
export class Report {
    constructor(
        public id?: number,
        public createdDate?: any,
        public modifyDate?: any,
        public reportContent?: ReportContent,
        public sprint?: Sprint,
    ) { }
}
