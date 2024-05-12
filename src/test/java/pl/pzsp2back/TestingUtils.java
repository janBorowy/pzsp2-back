package pl.pzsp2back;

import lombok.experimental.UtilityClass;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.stereotype.Component;

@UtilityClass
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestingUtils {

    @LocalServerPort
    private int port;
    private static String URL_PREFIX = "http://localhost:/";

    public String sendRequest(TestRestTemplate template, String path) {
        return template.getForObject(URL_PREFIX + port, String.class);
    }
}
