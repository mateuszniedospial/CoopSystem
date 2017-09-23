import {Observable} from "rxjs";
import {Response} from "@angular/http";
/**
 * Created by Master on 04.03.2017.
 */

export interface SearchService {
    queryFirstString(req?: any): Observable<Response>
}
