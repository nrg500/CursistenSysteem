package nl.berwout.api;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.context.embedded.LocalServerPort;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class FileImportSteps extends SpringIntegrationTest {

    @LocalServerPort
    int port;

    private WebDriver driver;

    @Given("^the client is on the import file page$")
    public void theClientIsOnTheImportFilePage() throws Throwable {
        String baseUrl = "http://localhost:" + port;
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\BerwoutV\\Downloads\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.get(baseUrl);
    }

    @When("^the client selects a correct file$")
    public void theClientSelectsAFile() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        WebElement fileInput = driver.findElement(By.name("file"));
        ClassLoader classLoader = getClass().getClassLoader();
        Path p = Paths.get(classLoader.getResource("GoedVoorbeeld.txt").toURI());
        fileInput.sendKeys(p.toAbsolutePath().toString());
    }

    @And("^the client clicks submit$")
    public void theClientClicksSubmit() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        WebElement submitButton = driver.findElement(By.id("select-file-submit"));
        submitButton.click();
    }

    @Then("^the client will receive a message that the import succeeded$")
    public void theClientWillReceiveAMessageThatTheImportSucceeded() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        WebElement successMessage = new WebDriverWait(driver, 20)
                .until(ExpectedConditions.presenceOfElementLocated(By.className("alert-success")));
        assertThat(successMessage.getText(), equalTo("Er zijn 4 cursusinstanties toegevoegd. Daarnaast zijn er 1 duplicaten gevonden."));
        driver.quit();
    }

    @When("^the client selects an incorrect file$")
    public void theClientSelectsAnIncorrectFile() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        WebElement fileInput = driver.findElement(By.name("file"));
        ClassLoader classLoader = getClass().getClassLoader();
        Path p = Paths.get(classLoader.getResource("FoutVoorbeeld5.txt").toURI());
        fileInput.sendKeys(p.toAbsolutePath().toString());
    }

    @Then("^the client will receive a message that specifies which line number went wrong$")
    public void theClientWillReceiveAMessageThatSpecifiesWhichLineNumberWentWrong() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        WebElement failMessage = new WebDriverWait(driver, 20)
                .until(ExpectedConditions.presenceOfElementLocated(By.className("alert-danger")));
        assertThat(failMessage.getText(), equalTo("Bestand is niet in correct formaat op regel 4. Er zijn geen cursusinstanties toegevoegd."));
        System.out.println(failMessage.getText());
        driver.quit();
    }
}
