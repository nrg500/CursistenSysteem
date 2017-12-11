package nl.berwout.api.controllers;

import nl.berwout.api.exceptions.InvalidFileFormatException;
import nl.berwout.api.models.CourseInstance;
import nl.berwout.api.models.DateCodeKey;
import nl.berwout.api.models.FileImport;
import nl.berwout.api.repositories.CourseInstanceRepository;
import nl.berwout.api.services.TextFileParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
public class TextFileImportController {

    private TextFileParser textFileparser;
    private CourseInstanceRepository courseInstanceRepository;

    @Autowired
    public TextFileImportController(TextFileParser textFileparser, CourseInstanceRepository courseInstanceRepository){
        this.textFileparser = textFileparser;
        this.courseInstanceRepository = courseInstanceRepository;
    }

    @CrossOrigin
    @RequestMapping(value="/api/file-import", method=RequestMethod.POST)
    public ResponseEntity<Collection<CourseInstance>> postNewFile(@RequestBody FileImport fileImport) throws InvalidFileFormatException{
        List<CourseInstance> courseInstances = textFileparser.parse(fileImport.getFileContents());
        List<CourseInstance> uniqueInstances = removeDuplicates(courseInstances);
        courseInstanceRepository.save(uniqueInstances);
        return new ResponseEntity<>(uniqueInstances, HttpStatus.CREATED);
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
