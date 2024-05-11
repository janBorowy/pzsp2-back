package pl.pzsp2back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("file:${user.dir}/.env")
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
