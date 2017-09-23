/**
 * Created by Master on 19.04.2017.
 */
export class DateUtil {

    public  static formatDate(inputDate){
        if(inputDate) {
            var local : Date = new Date(inputDate);
            return this.addZeroToDateIfRequired(local.getDate()) +'-'+
                this.addZeroToDateIfRequired((local.getMonth()+1)) +'-'+
                this.addZeroToDateIfRequired(local.getFullYear()) +' '+
                this.addZeroToDateIfRequired(local.getHours()) +':'+
                this.addZeroToDateIfRequired(local.getMinutes()) +':'+
                this.addZeroToDateIfRequired(local.getSeconds())
        }
    }

    public static formatToShortDate(inputDate) {
        if (inputDate) {
            var local: Date = new Date(inputDate);
            return this.addZeroToDateIfRequired(local.getDate()) + '-' +
                this.addZeroToDateIfRequired((local.getMonth() + 1))
        }
    }

    static addZeroToDateIfRequired(input: number) {
        if(input<10) return '0'+input;
        else return input;

    }
}
