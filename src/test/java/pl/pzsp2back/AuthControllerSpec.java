package pl.pzsp2back;

import static org.assertj.core.api.Assertions.assertThat;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import pl.pzsp2back.controllers.AuthController;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerSpec {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate template;
    @Autowired
    private AuthController authController;
    private String getPrefixURL() {
        return "http://localhost:" + port;
    }

    @Test
    void contextLoads() throws Exception {
        assertThat(authController).isNotNull();
    }

    @Test
    void canSignUp() throws Exception {
        var user = new JSONObject();
        user.put("login", "test");
        user.put("password", "user");
        user.put("email", "test@mail.com");
        user.put("name", "hello");
        user.put("surname", "there");
        user.put("groupId", 1);
        var headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        var request = new HttpEntity<String>(user.toString(), headers);

        var response = template.postForEntity(getPrefixURL() + "/auth/signup", request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void shouldResponseForbiddenIfUserExistsSignUp() throws Exception {
        var user = new JSONObject();
        user.put("login", "user1");
        user.put("password", "user");
        user.put("email", "somemail@mail.com");
        user.put("name", "hello");
        user.put("surname", "there");
        user.put("groupId", 1);
        var headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        var request = new HttpEntity<String>(user.toString(), headers);

        var response = template.postForEntity(getPrefixURL() + "/auth/signup", request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

}
