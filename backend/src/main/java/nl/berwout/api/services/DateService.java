package nl.berwout.api.services;

import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;


@Service
public class DateService {
    public Date[] getWeekStartAndEnd(int year, int week){
        Date[] result = new Date[2];
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        cal.set(Calendar.WEEK_OF_YEAR, week);
        result[0] = cal.getTime();
        cal.add(Calendar.DATE, 7);
        result[1] = cal.getTime();
        return result;
    }

    public boolean dateInCurrentYear(Date date, int year){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return year == cal.get(Calendar.YEAR);
    }

    public Date addXDaysToDate(Date date, int days){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }
}
