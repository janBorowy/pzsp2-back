package pl.pzsp2back.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pzsp2back.exceptions.UserServiceException;
import pl.pzsp2back.orm.User;
import pl.pzsp2back.services.UserService;

@RestController()
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{login}")
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

    @PutMapping("/")
    public ResponseEntity<?> putUser(@Valid @RequestBody User user) {
        try {
            var updatedUser = userService.updateUser(user.getLogin(), user);
            return ResponseEntity.ok(updatedUser);
        } catch (UserServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{login}")
    public ResponseEntity<?> deleteUser(@PathVariable("login") String login) {
        try {
            var deletedUser = userService.deleteUser(login);
            return ResponseEntity.ok(deletedUser);
        } catch (UserServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
