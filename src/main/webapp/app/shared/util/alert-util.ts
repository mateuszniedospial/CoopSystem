/**
 * Created by Master on 23.04.2017.
 */
export class AlertUtil {
    static createSuccessAlert(message: string, msgs: any[]) {
        msgs.push({severity:'success', summary:'Success', detail:message});
        setTimeout(()=>{
            msgs.pop();
        },2000);

    }

    static createErrorAlert(message: string, msgs: any[]) {
        msgs.push({severity:'error', summary:'Error', detail:message});
        setTimeout(()=>{
            msgs.pop();
        },2000);
    }
}
