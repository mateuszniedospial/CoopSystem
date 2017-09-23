/**
 * Created by Master on 31.05.2017.
 */

export class TaskIconUtil {
    public  static getTypeIcon(type:string){
        if(type=="BUG") {
            return '<span class="tag tag-danger"><b>B</b></span>'
        } else if(type=="EPIC"){
            return '<span class="tag tag-success"><b>E</b></span>'
        } else if(type=="NABUG"){
            return '<span class="tag tag-default"><b>N</b></span>'
        } else if(type=="TTASK"){
            return '<span class="tag tag-info"><b>T</b></span>'
        } else if(type=="STORY"){
            return '<span class="tag tag-warning"><b>S</b></span>'
        } else if(type =="DOCUTASK") {
            return '<span class="tag tag-primary"><b>D</b></span>'
        } else if(type =="ADMINTASK") {
            return '<span class="tag tag-primary"><b>A</b></span>'
        } else if(type =="ADHOC") {
            return '<span class="tag tag-primary"><b>H</b></span>'
        }
    }
}
