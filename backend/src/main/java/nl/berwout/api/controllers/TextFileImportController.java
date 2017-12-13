package nl.berwout.api.controllers;

import nl.berwout.api.exceptions.InvalidDateException;
import nl.berwout.api.exceptions.InvalidFileFormatException;
import nl.berwout.api.models.CourseInstance;
import nl.berwout.api.models.DateCodeKey;
import nl.berwout.api.models.FileImport;
import nl.berwout.api.repositories.CourseInstanceRepository;
import nl.berwout.api.services.CourseInstanceCollectionUtil;
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
    private CourseInstanceCollectionUtil courseInstanceCollectionUtil;

    @Autowired
    public TextFileImportController(TextFileParser textFileparser, CourseInstanceRepository courseInstanceRepository, CourseInstanceCollectionUtil courseInstanceCollectionUtil){
        this.textFileparser = textFileparser;
        this.courseInstanceRepository = courseInstanceRepository;
        this.courseInstanceCollectionUtil = courseInstanceCollectionUtil;
    }

    @RequestMapping(value="/api/file-import", method=RequestMethod.POST)
    public ResponseEntity<Collection<CourseInstance>> postNewFile(@RequestBody FileImport fileImport) throws InvalidFileFormatException, InvalidDateException{
        if(fileImport.getEndDate().before(fileImport.getStartDate())){
            throw new InvalidDateException("Einddatum mag niet voor de startdatum liggen.");
        }
        List<CourseInstance> courseInstances = textFileparser.parse(fileImport.getFileContents());
        List<CourseInstance> uniqueInstances = this.courseInstanceCollectionUtil.removeDuplicates(courseInstances);
        List<CourseInstance> filteredUniqueInstances = this.courseInstanceCollectionUtil
                .filterBetweenDateRange(uniqueInstances, fileImport.getStartDate(), fileImport.getEndDate());
        courseInstanceRepository.save(filteredUniqueInstances);
        return new ResponseEntity<>(filteredUniqueInstances, HttpStatus.CREATED);
    }
}
