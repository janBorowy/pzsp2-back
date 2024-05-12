package pl.pzsp2back.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.pzsp2back.exceptions.UserServiceException;
import pl.pzsp2back.orm.User;
import pl.pzsp2back.security.UserService;

@RestController()
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{login}")
    public ResponseEntity<?> findUserByLogin(@PathVariable("login") String login) {
        if (login.isEmpty()) {
            return ResponseEntity.badRequest().body("Login not specified");
        }
        try {
            var user = userService.getUser(login);
            return ResponseEntity.ok(user);
        } catch (UserServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/users")
    public ResponseEntity<?> putUser(@RequestBody User user) {
        try {
            var updatedUser = userService.updateUser(user.getLogin(), user);
            return ResponseEntity.ok(updatedUser);
        } catch (UserServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/users/{login}")
    public ResponseEntity<?> deleteUser(@PathVariable("login") String login) {
        try {
            var deletedUser = userService.deleteUser(login);
            return ResponseEntity.ok(deletedUser);
        } catch (UserServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
