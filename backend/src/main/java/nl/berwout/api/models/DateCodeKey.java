package nl.berwout.api.models;

import java.util.Date;

/**
 * used to make storing into a map and removing duplicates possible. (in TextFileParser)
 */
public class DateCodeKey {
    private Date date;
    private String courseCode;

    public DateCodeKey(Date date, String courseCode){
        this.date = date;
        this.courseCode = courseCode;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if(obj != null && obj instanceof DateCodeKey) {
            DateCodeKey s = (DateCodeKey)obj;
            result = (date.compareTo(s.date) == 0) && courseCode.equals(s.courseCode);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return date.hashCode() * 31 + courseCode.hashCode();
    }
}
