import {Component, OnInit, Input, Output, EventEmitter} from '@angular/core';
import {Response} from '@angular/http';
import {SearchService} from './search.service';

@Component({
    selector: 'mySearch',
    templateUrl: 'search.component.html',
    styleUrls: [
       'search.css'
    ]
})
export class SearchComponent implements OnInit{
    ngOnInit(): void {
    }

    inputText: string;
    results: string[];
    @Output() resultEvent = new EventEmitter<any>();
    result :any;
    @Input() searchService : SearchService;
    @Input() size : number;
    constructor() {}

    onSubmitSearch(){
        this.searchByString(this.inputText, true);
    }

    searchByString(input: string, isSubmit: boolean) {

        this.searchService.queryFirstString({
            'query':input
        }).subscribe(
            (res: Response) => {
                var response: any[];

                response = res.json();
                console.log(response);
                this.results = [''];
                var i =0;
                response.forEach(it=>{
                    this.results[i++] = it.name;
                })
                if (isSubmit) {
                    this.result = response;
                    this.resultEvent.emit(this.result);
                }
            },
            (res: Response) => this.onError(res.json())
        );
    }

    private onError (error) {
        console.log(error.message);
    }

    search(event:any) {
        console.log(this.inputText);
        this.searchByString(this.inputText, false)
    }



}
