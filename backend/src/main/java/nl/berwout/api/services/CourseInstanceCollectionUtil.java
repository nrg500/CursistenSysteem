package nl.berwout.api.services;

import nl.berwout.api.models.CourseInstance;
import nl.berwout.api.models.DateCodeKey;
import nl.berwout.api.repositories.CourseInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Helper utility for managing Lists of CourseInstances
 */
@Service
public class CourseInstanceCollectionUtil {
    private CourseInstanceRepository courseInstanceRepository;
    private DateService dateService;
    @Autowired
    public CourseInstanceCollectionUtil(CourseInstanceRepository courseInstanceRepository, DateService dateService){
        this.courseInstanceRepository = courseInstanceRepository;
        this.dateService = dateService;
    }

    /**
     * Filters a list of CourseInstances by removing instances that start after the enddate or end after the startdate.
     * @param instances list of CourseInstances you want filtered
     * @param startDate start of the date range
     * @param endDate end of the date range
     * @return filtered list of CourseInstances
     */
    public List<CourseInstance> filterBetweenDateRange(List<CourseInstance> instances, Date startDate, Date endDate) {
        return instances.stream()
                .filter(courseInstance -> {
                    Date ciEndDate = dateService.addXDaysToDate(courseInstance.getStartDate(), courseInstance.getDuration());
                    return courseInstance.getStartDate().before(endDate) && ciEndDate.after(startDate);
                })
                .collect(Collectors.toList());
    }

    /**
     * removes duplicates by storing the courseinstances into a map with a combination of startdate and coursecode as key.
     * after the mapping, finds if there are any duplicates in the database and removes those.
     * @param inputList list of CourseInstances
     * @return unique list of CourseInstances
     */
    public List<CourseInstance> removeDuplicates(List<CourseInstance> inputList){
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
