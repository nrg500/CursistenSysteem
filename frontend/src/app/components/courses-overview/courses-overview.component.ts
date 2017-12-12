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
  private year: number;
  private week : number;

  constructor(private http: HttpClient) {
    this.interval = Observable.interval(5000)
      .switchMap(() =>
        http.get(`/api/course-instances/${this.year}/${this.week}`))
      .subscribe(
        (data: CourseInstance[]) => {
          data.map(ci => ci.startDate = new Date(ci.startDate));
          this.sortedCourseInstances = data.sort((ci1, ci2) => this.sortCourseInstancesByDate(ci2.startDate, ci1.startDate));
        },
        (err: HttpErrorResponse) => console.log(err.error));
  }

  getWeekNumber(date: Date){
    let d = new Date(Date.UTC(date.getFullYear(), date.getMonth(), date.getDate()));
    let dayNum = d.getUTCDay() || 7;
    d.setUTCDate(d.getUTCDate() + 4 - dayNum);
    let yearStart = new Date(Date.UTC(d.getUTCFullYear(),0,1));
    //86400000 is full day, so we calculate amount of days + 1
    // (because of yearStart being the first), divided by 7 to get weeks.
    return Math.ceil((((d.getTime() - yearStart.getTime()) / 86400000) + 1)/7)
  };

  ngOnInit() {
    this.week = this.getWeekNumber(new Date());
    this.year = new Date().getUTCFullYear();
    this.initialGet = this.http.get(`/api/course-instances/${this.year}/${this.week}`)
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
