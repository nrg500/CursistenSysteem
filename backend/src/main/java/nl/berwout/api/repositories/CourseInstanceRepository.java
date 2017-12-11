package nl.berwout.api.repositories;

import nl.berwout.api.models.CourseInstance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseInstanceRepository extends JpaRepository<CourseInstance, Integer> {
}
