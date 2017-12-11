export class CourseInstance{
  constructor(
    public id: number = 0,
    public courseCode: string = "",
    public startDate: Date,
    public duration: number = 0,
    public title: string = ""
  ) {}
}
