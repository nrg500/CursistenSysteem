import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {SelectFileComponent} from "./components/select-file/select-file.component";
import {CoursesOverviewComponent} from "./components/courses-overview/courses-overview.component";

const routes: Routes = [
  { path: '', component: CoursesOverviewComponent },
  { path: 'import-file', component: SelectFileComponent }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule { }
