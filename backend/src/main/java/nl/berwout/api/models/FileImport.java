package nl.berwout.api.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nl.berwout.api.services.CustomJsonDeserializer;

import java.util.Date;

public class FileImport {
    private String fileContents;
    private Date startDate;
    private Date endDate;

    public String getFileContents() {
        return fileContents;
    }

    public void setFileContents(String fileContents) {
        this.fileContents = fileContents;
    }

    public Date getStartDate() {
        return startDate;
    }

    @JsonDeserialize(using= CustomJsonDeserializer.class)
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    @JsonDeserialize(using= CustomJsonDeserializer.class)
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
