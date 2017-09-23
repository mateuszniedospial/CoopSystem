import {Message} from "primeng/components/common/api";
import {JUser} from "../../entities/j-user/j-user.model";
import {JGroup} from "../../entities/j-group/j-group.model";
import {Sprint} from "../../entities/sprint/sprint.model";
import {DateUtil} from "../../shared/util/date-util";
import {AlertUtil} from "../../shared/util/alert-util";
import {Response} from "@angular/http";
import {SprintService} from "../../entities/sprint/sprint.service";
import {JUserService} from "../../entities/j-user/j-user.service";
import {JGroupService} from "../../entities/j-group/j-group.service";

export class AgileBase {
    public msgs: Message[] = [];
    public activJUser: JUser;
    public jGroup:JGroup;
    public sprint: Sprint;

    constructor(protected sprintService: SprintService, protected jUserService: JUserService, protected jGroupService : JGroupService){}

     formatDate(input: any) {
        return DateUtil.formatDate(input);
    }

    protected createSprint(sprint:Sprint, onSuccess?:any) {
        this.sprintService.create(sprint)
            .subscribe(
                (res: Response) => {
                    AlertUtil.createSuccessAlert('Sprint created successful', this.msgs);
                    this.sprint = res;
                    onSuccess();
                },
                () => {AlertUtil.createErrorAlert('Try again', this.msgs)});
    }


    protected updateSprint(sprint:Sprint, onSuccess){
        this.sprintService.update(sprint)
            .subscribe(
                (res:Response)=>{
                    AlertUtil.createSuccessAlert('Sprint updated successful', this.msgs);
                    onSuccess();
                    this.sprint=res;},
                ()=>{
                    AlertUtil.createErrorAlert('Try again', this.msgs);})
    }

    protected initJUserIDByJhiId(jhiId : number ){
        this.jUserService.query({
            'jhiId' : jhiId})
            .subscribe(
                (res: Response) => {this.activJUser =res.json();},
                (res: Response) => this.onError(res.json()))
    }

    protected initJGroupById(id) {
        this.jGroupService.find(id).subscribe(
            (res: Response)=>{
                this.jGroup = res
            },
            (res: Response)=>{this.onError(res)})
    }

    protected onError(any?: any) {
        console.log("Application error in script")
    }
}
