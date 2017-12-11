package nl.berwout.api.models;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class CourseInstance extends Course {
    private Date startDate;

    public CourseInstance(){
        super();
    }

    public CourseInstance(String title, String courseCode, byte duration, Date startDate){
        super(title, courseCode, duration);
        this.startDate = startDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
