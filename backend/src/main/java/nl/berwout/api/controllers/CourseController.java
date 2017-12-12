package nl.berwout.api.controllers;

import nl.berwout.api.exceptions.InvalidInputException;
import nl.berwout.api.models.CourseInstance;
import nl.berwout.api.repositories.CourseInstanceRepository;
import nl.berwout.api.services.DateService;
import org.omg.CORBA.DynAnyPackage.Invalid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class CourseController {

    private CourseInstanceRepository courseInstanceRepository;
    private DateService dateService;

    @Autowired
    public CourseController(CourseInstanceRepository courseInstanceRepository, DateService dateService){
        this.courseInstanceRepository = courseInstanceRepository;
        this.dateService = dateService;
    }

    @CrossOrigin
    @RequestMapping(value="/api/course-instances", method=RequestMethod.GET)
    public ResponseEntity<List<CourseInstance>> getCourseInstances(){
        return new ResponseEntity<>(courseInstanceRepository.findAll(), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value="/api/course-instances/{year}/{week}", method=RequestMethod.GET)
    public ResponseEntity<List<CourseInstance>> getCourseInstancesForYearAndWeek(@PathVariable Integer year,@PathVariable Integer week) throws InvalidInputException{

        if(year > 2100 || year < 1990){
            throw new InvalidInputException("Year must be between 1990 and 2100.");
        }
        if(week < 1 || week > 54){
            throw new InvalidInputException("Week must be between 1 and 54");
        }
        Date[] dates = dateService.getWeekStartAndEnd(year, week);
        //als de begin en einddatum in het nieuwe jaar vallen, is het ook niet in orde.
        if((!dateService.dateInCurrentYear(dates[0], year)) && (!dateService.dateInCurrentYear(dates[1], year))){
            throw new InvalidInputException("Week must be in the current year");
        }
        return new ResponseEntity<>(courseInstanceRepository.findByStartDateBetween(dates[0], dates[1]), HttpStatus.OK);
    }
}
