
export const enum SprintLifeCycle {
    'ACTIVE',
    'OUTDATED',
    'FUTURE'

};
export const enum SprintEstimateRemainingAction{
    'PLUS',
    'MINUS'
}
import { JGroup } from '../j-group';
export class Sprint {
    constructor(
        public id?: number,
        public title?: string,
        public createdDate?: any,
        public modifyDate?: any,
        public lifeCycle?: SprintLifeCycle,
        public startTime?: any,
        public stopTime?: any,
        public durationTime?: number,
        public sumOfEstimate?: number,
        public sumOfRemaining?: number,
        public description?: any,
        public retrospective?: string,
        public jgroup?: JGroup,
    ) { }
}
