import {Component, OnDestroy} from '@angular/core';
import {FileImport} from "../../models/file-import";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {CourseInstance} from "../../models/course-instance";
import {ISubscription} from "rxjs/Subscription";
import {MAT_DATE_FORMATS} from "@angular/material";
import {Form, FormControl, FormGroup, Validators} from "@angular/forms";

export const MY_FORMATS = {
  parse: {
    dateInput: 'DD/MM/YYYY'
  },
  display: {
    dateInput: 'DD/MM/YYYY',
    monthYearLabel: 'MMM YYYY'
  },
};

@Component({
  selector: 'file-select',
  templateUrl: './select-file.component.html',
  styleUrls: ['./select-file.component.css'],
  providers: [
    {provide: MAT_DATE_FORMATS, useValue: MY_FORMATS}
  ]
})

export class SelectFileComponent implements OnDestroy{
  private model : FileImport = new FileImport();
  // private file : File = null;
  private successMessage : string;
  private errorMessage : string;
  private expectedImports : number;
  private actualImports : number;
  private file : File;
  private importPost : ISubscription;
  private form: FormGroup;

  constructor(private http: HttpClient){
    this.form = new FormGroup ({
      startDate: new FormControl('', [Validators.required]),
      endDate: new FormControl('', [Validators.required])
    });
  }

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
    if(this.form.valid && this.file) {
      if (this.form.value.startDate && this.form.value.endDate && (this.form.value.startDate < this.form.value.endDate)) {
        let split: string[] = this.model.fileContents.split(/^\s[\r\n]/gm);
        if (split[split.length] === "") {
          split.splice(-1, 1);
        }
        this.expectedImports = split.length;
        this.model.startDate = this.form.value.startDate.format("DD/MM/YYYY");
        this.model.endDate = this.form.value.endDate.format("DD/MM/YYYY");
        this.importPost = this.http.post("/api/file-import", this.model)
          .subscribe(
            (data: Array<CourseInstance>) => {
              this.actualImports = data.length;
              this.successMessage = `Er zijn ${data.length} cursusinstanties toegevoegd.`;
              if (this.actualImports != this.expectedImports) {
                this.successMessage += `\nDaarnaast zijn er ${this.expectedImports - this.actualImports} genegeerd, omdat ze dubbel waren of buiten de aangegeven tijdstippen vielen.`;
              }
              console.log(data);
            },
            (err: HttpErrorResponse) => {
              if (typeof err.error === "string") {
                this.errorMessage = err.error;
              } else {
                //relevant if server doesn't answer.
                this.errorMessage = "Er is iets misgegaan, probeer het later nog een keer.";
              }
            }
          );
      } else {
        this.errorMessage = "De startdatum moet voor de einddatum liggen.";
      }
    }else{
      this.errorMessage = "Vul aub. alle waardes in.";
    }
  }



  ngOnDestroy(){
    this.importPost.unsubscribe();
  }
}
