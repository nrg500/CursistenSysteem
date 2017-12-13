package nl.berwout.api.controllers;

import nl.berwout.api.exceptions.InvalidDateException;
import nl.berwout.api.exceptions.InvalidFileFormatException;
import nl.berwout.api.models.CourseInstance;
import nl.berwout.api.models.DateCodeKey;
import nl.berwout.api.models.FileImport;
import nl.berwout.api.repositories.CourseInstanceRepository;
import nl.berwout.api.services.DateService;
import nl.berwout.api.services.TextFileParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
public class TextFileImportController {

    private TextFileParser textFileparser;
    private CourseInstanceRepository courseInstanceRepository;
    private DateService dateService;

    @Autowired
    public TextFileImportController(TextFileParser textFileparser, CourseInstanceRepository courseInstanceRepository, DateService dateService){
        this.textFileparser = textFileparser;
        this.courseInstanceRepository = courseInstanceRepository;
        this.dateService = dateService;
    }

    @RequestMapping(value="/api/file-import", method=RequestMethod.POST)
    public ResponseEntity<Collection<CourseInstance>> postNewFile(@RequestBody FileImport fileImport) throws InvalidFileFormatException, InvalidDateException{
        System.out.println(fileImport.getStartDate());
        System.out.println(fileImport.getEndDate());
        if(fileImport.getEndDate().before(fileImport.getStartDate())){
            throw new InvalidDateException("Einddatum mag niet voor de startdatum liggen.");
        }
        List<CourseInstance> courseInstances = textFileparser.parse(fileImport.getFileContents());
        List<CourseInstance> uniqueInstances = removeDuplicates(courseInstances);
        List<CourseInstance> filteredUniqueInstances = removeOutsideOfRange(uniqueInstances, fileImport.getStartDate(), fileImport.getEndDate());
        courseInstanceRepository.save(filteredUniqueInstances);
        return new ResponseEntity<>(filteredUniqueInstances, HttpStatus.CREATED);
    }

    private List<CourseInstance> removeOutsideOfRange(List<CourseInstance> uniqueInstances, Date startDate, Date endDate) {
        return uniqueInstances.stream()
                .filter(courseInstance -> {
                    Date ciEndDate = dateService.addXDaysToDate(courseInstance.getStartDate(), courseInstance.getDuration());
                    return courseInstance.getStartDate().before(endDate) && ciEndDate.after(startDate);
                })
                .collect(Collectors.toList());
    }

    //removes duplicates by first converting to map and then finding duplicates in the database and filtering the list.
    private List<CourseInstance> removeDuplicates(List<CourseInstance> inputList){
        Map<DateCodeKey, CourseInstance> uniqueMap = inputList.stream()
                .collect(
                        Collectors.toMap(
                                courseInstance -> new DateCodeKey(courseInstance.getStartDate(), courseInstance.getCourseCode())
                                , Function.identity()
                                //merge strategy to handle duplicate keys.
                                , (oldValue, newValue) -> oldValue
                        )
                );
        return uniqueMap.values().stream()
                .filter(courseInstance -> {
                    List<CourseInstance> duplicates =
                            courseInstanceRepository.findByStartDateAndCourseCode(courseInstance.getStartDate(), courseInstance.getCourseCode());
                    return duplicates.size() == 0;
                }).collect(Collectors.toList());
    }
}
