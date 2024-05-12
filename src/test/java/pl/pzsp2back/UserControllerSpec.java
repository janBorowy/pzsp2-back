package pl.pzsp2back;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import pl.pzsp2back.controllers.UserController;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerSpec {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private UserController userController;

    private String getPrefixURL() {
        return "http://localhost:" + port;
    }

    @Test
    void contextLoads() throws Exception {
        assertThat(userController).isNotNull();
    }

    @Test
    void getUser() throws Exception {
        String response = template.getForObject(getPrefixURL() + "/users/user1", String.class);
        assertThat(
                response.equals("{\"login\":\"user1\",\"hashedPassword\":\"password1\",\"email\":\"user1@example.com\",\"name\":\"John\",\"surname\":\"Doe\",\"groupId\":1,\"password\":\"password1\",\"enabled\":true,\"username\":\"user1\",\"admin\":false,\"authorities\":[],\"accountNonExpired\":true,\"accountNonLocked\":true,\"credentialsNonExpired\":true}\n")
        );
    }
}
