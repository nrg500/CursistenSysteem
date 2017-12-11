package nl.berwout.api.controllers;

import nl.berwout.api.exceptions.InvalidFileFormatException;
import nl.berwout.api.models.CourseInstance;
import nl.berwout.api.models.FileImport;
import nl.berwout.api.repositories.CourseInstanceRepository;
import nl.berwout.api.services.TextFileParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<CourseInstance>> postNewFile(@RequestBody FileImport fileImport) throws InvalidFileFormatException{
        List<CourseInstance> courseInstances = textFileparser.parse(fileImport.getFileContents());
        courseInstanceRepository.save(courseInstances);
        return new ResponseEntity<>(courseInstances, HttpStatus.CREATED);
    }
}
