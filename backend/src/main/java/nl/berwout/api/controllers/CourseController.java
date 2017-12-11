package nl.berwout.api.controllers;

import nl.berwout.api.models.CourseInstance;
import nl.berwout.api.repositories.CourseInstanceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CourseController {

    private CourseInstanceRepository courseInstanceRepository;

    public CourseController(CourseInstanceRepository courseInstanceRepository){
        this.courseInstanceRepository = courseInstanceRepository;
    }

    @CrossOrigin
    @RequestMapping(value="/api/course-instances", method= RequestMethod.GET)
    public ResponseEntity<List<CourseInstance>> getCourseInstances(){
        return new ResponseEntity<>(courseInstanceRepository.findAll(), HttpStatus.OK);
    }
}
