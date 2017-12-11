package nl.berwout.api.models;

import javax.persistence.*;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class Course {
    @Id @GeneratedValue
    private int id;
    private String courseCode;
    private byte duration;
    private String title;

    public Course(){}

    public Course(String title, String courseCode, byte duration){
        this.title = title;
        this.courseCode = courseCode;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte getDuration() {
        return duration;
    }

    public void setDuration(byte duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCourseCode() {
        return courseCode;
    }
}
