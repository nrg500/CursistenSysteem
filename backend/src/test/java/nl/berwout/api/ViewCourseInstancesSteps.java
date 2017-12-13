package nl.berwout.api;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import nl.berwout.api.models.CourseInstance;
import nl.berwout.api.repositories.CourseInstanceRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

public class ViewCourseInstancesSteps extends SpringIntegrationTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @Autowired
    private CourseInstanceRepository courseInstanceRepository;

    @Autowired
    private EntityManager em;

    @Given("^the database contains three course instances$")
    @Transactional
    public void theDatabaseContainsThreeCourseInstances() throws Throwable {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaDelete<CourseInstance> query = builder.createCriteriaDelete(CourseInstance.class);
        query.from(CourseInstance.class);
        em.createQuery(query).executeUpdate();
        // Write code here that turns the phrase above into concrete actions
        courseInstanceRepository.save(new CourseInstance("Coursey", "ABCDEF", (byte)4, new Date(System.currentTimeMillis())));
        courseInstanceRepository.save(new CourseInstance("Coursey2", "ABCDEF", (byte)5, new Date(System.currentTimeMillis())));
        courseInstanceRepository.save(new CourseInstance("Coursey3", "ABCDEF", (byte)2, new Date(System.currentTimeMillis())));
        courseInstanceRepository.flush();
    }

    @Given("^the client has an open browser window$")
    public void theClientHasAnOpenBrowserWindow() throws Throwable {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\BerwoutV\\Downloads\\chromedriver.exe");
        driver = new ChromeDriver();
    }

    @When("^the client navigates to the list course instances page$")
    public void theClientNavigatesToTheListCourseInstancesPage() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        String baseUrl = "http://localhost:" + port;
        driver.get(baseUrl);
    }

    @Then("^the client sees all the course instances$")
    public void theClientSeesAllTheCourseInstances() throws Throwable {
        //wait for courses to appear
        WebElement course = new WebDriverWait(driver, 20)
                .until(ExpectedConditions.presenceOfElementLocated(By.className("course-instance")));
        List<WebElement> courses = driver.findElements(By.className("course-instance"));
        assertThat(courses.size(), is(equalTo(3)));
    }

}
