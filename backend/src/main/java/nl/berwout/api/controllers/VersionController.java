package nl.berwout.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VersionController {
    @RequestMapping(method={RequestMethod.GET},value={"/api/version"})
    public String getVersion() {
        return "1.0";
    }
}