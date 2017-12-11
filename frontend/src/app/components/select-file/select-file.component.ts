import {Component, OnDestroy} from '@angular/core';
import {FileImport} from "../../models/file-import";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {CourseInstance} from "../../models/course-instance";
import {ISubscription} from "rxjs/Subscription";

@Component({
  selector: 'file-select',
  templateUrl: './select-file.component.html',
  styleUrls: ['./select-file.component.css']
})

export class SelectFileComponent implements OnDestroy{
  private model : FileImport = new FileImport();
  private file : File = null;
  private successMessage : string;
  private errorMessage : string;
  private expectedImports : number;
  private actualImports : number;

  private importPost : ISubscription;
  constructor(private http: HttpClient){}

  onChange(event: EventTarget) {
    let eventObj: MSInputMethodContext = <MSInputMethodContext> event;
    let target: HTMLInputElement = <HTMLInputElement> eventObj.target;
    this.file = target.files[0];
    let fileReader = new FileReader();
    if(this.file instanceof Blob && this.file.name.endsWith(".txt")) {
      fileReader.readAsText(this.file);
      //try to read file, this part does not work at all, need a solution
      fileReader.onloadend = () => {
        console.log(fileReader.result);
        this.model.fileContents = fileReader.result;
      };
    }
  }

  submit() {
    this.successMessage = null;
    this.errorMessage = null;
    let split : string[] = this.model.fileContents.split(/^\s[\r\n]/gm);
    if(split[split.length] === ""){
      split.splice(-1,1);
    }
    this.expectedImports = split.length;
    console.log(`expected imports: ${this.expectedImports}`);
    this.importPost = this.http.post("http://localhost:8080/api/file-import", this.model)
      .subscribe(
        (data: Array<CourseInstance>) => {
          this.actualImports = data.length;
          console.log(`actual imports: ${this.actualImports}`);
          this.successMessage = `Er zijn ${data.length} cursusinstanties toegevoegd.`;
          if(this.actualImports != this.expectedImports){
            this.successMessage += `\nDaarnaast zijn er ${this.expectedImports - this.actualImports} duplicaten gevonden.`;
          }
          console.log(data);
      },
        (err: HttpErrorResponse) => {
          this.errorMessage = err.error;
          console.log(err.error);
        }
      );
  }

  ngOnDestroy(){
    this.importPost.unsubscribe();
  }
}
