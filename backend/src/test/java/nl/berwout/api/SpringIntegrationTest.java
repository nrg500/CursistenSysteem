package nl.berwout.api;

import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ContextConfiguration(
        classes = ApiApplication.class,
        loader = SpringBootContextLoader.class)
@SpringBootTest(webEnvironment=RANDOM_PORT)
public class SpringIntegrationTest {

}
