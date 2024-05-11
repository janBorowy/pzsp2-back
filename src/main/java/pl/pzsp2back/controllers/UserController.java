package pl.pzsp2back.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.pzsp2back.orm.User;
import pl.pzsp2back.orm.UserRepository;

@RestController()
@AllArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/users")
    @ResponseBody
    public Iterable<User> findAllUsers() {
        return this.userRepository.findAll();
    }

    @GetMapping("/users/{login}")
    @ResponseBody
    public User findUserById(@PathVariable("login") String login) {
        return userRepository.findById(login)
                .orElseThrow();
    }
}
