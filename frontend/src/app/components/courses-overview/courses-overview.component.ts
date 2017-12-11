import {Component, OnDestroy, OnInit} from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {CourseInstance} from "../../models/course-instance";
import {Observable} from "rxjs/Rx";
import {ISubscription} from "rxjs/Subscription";

@Component({
  selector: 'courses-overview',
  templateUrl: './courses-overview.component.html',
  styleUrls: ['./courses-overview.component.css']
})

export class CoursesOverviewComponent implements OnInit, OnDestroy {
  private sortedCourseInstances: Array<CourseInstance> = [];

  private interval : ISubscription;
  private initialGet : ISubscription;

  constructor(private http: HttpClient) {
    this.interval = Observable.interval(5000)
      .switchMap(() =>
        http.get('/api/course-instances'))
      .subscribe(
        (data: CourseInstance[]) => {
          data.map(ci => ci.startDate = new Date(ci.startDate));
          this.sortedCourseInstances = data.sort((ci1, ci2) => this.sortCourseInstancesByDate(ci2.startDate, ci1.startDate));
        },
        (err: HttpErrorResponse) => console.log(err.error));
  }

  ngOnInit() {
    this.initialGet = this.http.get('/api/course-instances')
      .subscribe(
        (data: CourseInstance[]) => {
          data.map(ci => ci.startDate = new Date(ci.startDate));
          this.sortedCourseInstances = data.sort((ci1, ci2) => this.sortCourseInstancesByDate(ci2.startDate, ci1.startDate));
        },
        (err: HttpErrorResponse) => console.log(err.error));
  }

  sortCourseInstancesByDate(a : Date, b : Date){
    return a>b ? -1 : a<b ? 1 : 0;
  }

  ngOnDestroy() {
    this.interval.unsubscribe();
    this.initialGet.unsubscribe();
  }
}
