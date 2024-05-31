package pl.pzsp2back.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pzsp2back.dto.DtoMapper;
import pl.pzsp2back.dto.UserDto;
import pl.pzsp2back.exceptions.UserServiceException;
import pl.pzsp2back.orm.Group;
import pl.pzsp2back.orm.User;
import pl.pzsp2back.services.UserService;

import java.util.List;

@RestController()
@AllArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;
    private final DtoMapper dtoMapper;

    @GetMapping("/{login}")
    public ResponseEntity<?> findUserByLogin(@PathVariable("login") String login) {
        log.info("Fetch user %s");
        if (login.isEmpty()) {
            return ResponseEntity.badRequest().body("Login not specified");
        }
        try {
            var userDto = dtoMapper.toDto(userService.getUser(login));
            return ResponseEntity.ok(userDto);
        } catch (UserServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/")
    public ResponseEntity<?> putUser(@Valid @RequestBody User user) {
        try {
            var updatedUserDto = dtoMapper.toDto(userService.updateUser(user.getLogin(), user));
            return ResponseEntity.ok(updatedUserDto);
        } catch (UserServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{login}")
    public ResponseEntity<?> deleteUser(@PathVariable("login") String login) {
        try {
            var deletedUserDto = dtoMapper.toDto(userService.deleteUser(login));
            return ResponseEntity.ok(deletedUserDto);
        } catch (UserServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
