package nl.berwout.api.controllers;

import nl.berwout.api.exceptions.InvalidInputException;
import nl.berwout.api.models.CourseInstance;
import nl.berwout.api.repositories.CourseInstanceRepository;
import nl.berwout.api.services.DateService;
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

    @RequestMapping(value="/api/course-instances/{year}/{week}", method=RequestMethod.GET)
    public ResponseEntity<List<CourseInstance>> getCourseInstancesForYearAndWeek(@PathVariable Integer year,@PathVariable Integer week) throws InvalidInputException{
        if(year > 2200 || year < 1990){
            throw new InvalidInputException("Het jaar moet tussen 1990 en 2200 liggen.");
        }
        if(week < 1 || week > 54){
            throw new InvalidInputException("De week moet tussen de 1 en 54 liggen.");
        }
        Date[] dates = dateService.getWeekStartAndEnd(year, week);
        if((!dateService.dateInCurrentYear(dates[0], year)) && (!dateService.dateInCurrentYear(dates[1], year))){
            throw new InvalidInputException("De week moet in het huidige jaar vallen.");
        }
        return new ResponseEntity<>(courseInstanceRepository.findByStartDateBetween(dates[0], dates[1]), HttpStatus.OK);
    }
}
