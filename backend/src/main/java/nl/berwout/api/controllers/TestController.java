package nl.berwout.api.controllers;

import nl.berwout.api.annotations.Timed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
    @Timed
    @RequestMapping("/test")
    public ResponseEntity<String> getTest(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<String>("test", HttpStatus.OK);
    }

    @Timed
    @RequestMapping("/nina")
    public ResponseEntity<String> getNina(){
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<String>("Nina", HttpStatus.OK);
    }
}
