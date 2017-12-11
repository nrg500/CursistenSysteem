package nl.berwout.api.repositories;

import nl.berwout.api.models.CourseInstance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface CourseInstanceRepository extends JpaRepository<CourseInstance, Integer> {
    List<CourseInstance> findByStartDateAndCourseCode(Date startDate, String courseCode);
}
