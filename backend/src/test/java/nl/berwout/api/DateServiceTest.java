package nl.berwout.api;

import nl.berwout.api.services.DateService;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateServiceTest {
    private DateService dateService = new DateService();

    @Test
    public void todayInCurrentYear(){
        assertThat(dateService.dateInYear(Calendar.getInstance().getTime(), Calendar.getInstance().get(Calendar.YEAR)), is(true));
    }

    @Test
    public void todayNotIn2200(){
        assertThat(dateService.dateInYear(Calendar.getInstance().getTime(), 2200), is(false));
    }

    @Test
    public void addZeroDaysToDate(){
        Date d = Calendar.getInstance().getTime();
        assertThat(dateService.addXDaysToDate(d, 0), equalTo(d));
    }

    @Test
    public void addDaysToDateToCrossYearBorder(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2017);
        cal.set(Calendar.MONTH, 11);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        Date d = cal.getTime();
        Date dateAdded = dateService.addXDaysToDate(d, 1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(dateAdded);
        assertThat(cal2.get(Calendar.YEAR), is(cal.get(Calendar.YEAR) + 1));
    }

    @Test
    public void getThisWeeksStartAndEnd(){
        Calendar cal = Calendar.getInstance();
        Date[] startAndEnd = dateService.getWeekStartAndEnd(cal.get(Calendar.YEAR), cal.get(Calendar.WEEK_OF_YEAR));
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(startAndEnd[0]);
        end.setTime(startAndEnd[1]);
        long diff = end.getTime().getTime() - start.getTime().getTime();
        long diffInDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        assertThat(start.get(Calendar.DAY_OF_WEEK), is(Calendar.MONDAY));
        assertThat(end.get(Calendar.DAY_OF_WEEK), is(Calendar.MONDAY));
        assertThat(diffInDays, is(7L));
        assert(cal.getTime().after(start.getTime()) && cal.getTime().before(end.getTime()));
    }
}
