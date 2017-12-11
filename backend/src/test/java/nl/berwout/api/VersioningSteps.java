package nl.berwout.api;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class VersioningSteps extends SpringIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private ResponseEntity<String> response;

    @When("^the client calls /api/version$")
    public void the_client_issues_GET_version() throws Throwable{
        response = restTemplate.getForEntity("/api/version", String.class);
    }

    @Then("^the client receives status code of (\\d+)$")
    public void the_client_receives_status_code_of(int expectedStatusCode) throws Throwable {
        int actualStatusCode = response.getStatusCodeValue();
        assertThat("status code is incorrect : ", actualStatusCode, is(expectedStatusCode));
    }

    @And("^the client receives server version (.+)$")
    public void the_client_receives_server_version_body(String expectedVersion) throws Throwable {
        assertThat(response.getBody(), is(expectedVersion));
    }


}
