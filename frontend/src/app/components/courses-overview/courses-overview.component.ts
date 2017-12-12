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
  private getRequest : ISubscription;
  private errorMessage : string;
  private year: number;
  private week: number;

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
    this.week = this.getWeekNumber(new Date());
    this.year = new Date().getUTCFullYear();
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

  ngOnInit(){
    this.getInstances();
  }

  getInstances(){
    if(this.getRequest){
      this.getRequest.unsubscribe();
    }
    this.getRequest = this.http.get(`/api/course-instances/${this.year}/${this.week}`)
      .subscribe(
        (data: CourseInstance[]) => {
          data.map(ci => ci.startDate = new Date(ci.startDate));
          this.sortedCourseInstances = data.sort((ci1, ci2) => this.sortCourseInstancesByDate(ci2.startDate, ci1.startDate));
        },
        (err: HttpErrorResponse) => {
          if(typeof err.error === "string") {
            this.errorMessage = err.error;
          } else{
            //relevant if server doesn't answer.
            this.errorMessage = "Er is iets misgegaan, probeer het later nog een keer.";
          }
        }
      );
    }
  onChange() {
    this.errorMessage = "";
    this.getInstances();
  }


  weeksInYear(year) {
    var month = 11, day = 31, week;
    // Find week that 31 Dec is in. If is first week, reduce date until
    // get previous week.
    do {
      let d : Date = new Date(year, month, day--);
      week = this.getWeekNumber(d);
    } while (week == 1);

    return week;
  }
  previousWeek(){
    if(this.week === 1){
      this.week = this.weeksInYear(--this.year);
    } else{
      this.week--;
    }
    this.errorMessage = "";
    this.getInstances();
  }

  nextWeek(){
    if(this.week === this.weeksInYear(this.year)){
      this.week = 1;
      this.year++;
    } else{
      this.week++;
    }
    this.errorMessage = "";
    this.getInstances();
  }

  sortCourseInstancesByDate(a : Date, b : Date){
    return a>b ? -1 : a<b ? 1 : 0;
  }

  ngOnDestroy() {
    this.interval.unsubscribe();
    this.getRequest.unsubscribe();
  }
}
