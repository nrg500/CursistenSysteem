import {Component, Input} from '@angular/core';
import {CourseInstance} from "../../../models/course-instance";

@Component({
  selector: '[course-instance]',
  templateUrl: './course-instance.component.html',
  styleUrls: ['./course-instance.component.css']
})

export class CourseInstanceComponent{
  @Input() courseInstance : CourseInstance;
}
