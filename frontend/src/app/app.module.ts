import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {FormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";

import { AppComponent } from './app.component';
import {SelectFileComponent} from "./components/select-file/select-file.component";
import {CoursesOverviewComponent} from "./components/courses-overview/courses-overview.component";
import {CourseInstanceComponent} from "./components/courses-overview/course-instance/course-instance.component";
import {AppRoutingModule} from "./app-routing.module";

@NgModule({
  declarations: [
    AppComponent,
    SelectFileComponent,
    CoursesOverviewComponent,
    CourseInstanceComponent
  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [ ],
  bootstrap: [AppComponent]
})
export class AppModule { }
