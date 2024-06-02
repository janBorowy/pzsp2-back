package pl.pzsp2back.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.pzsp2back.dto.DtoMapper;
import pl.pzsp2back.dto.UserDto;
import pl.pzsp2back.exceptions.UserServiceException;
import pl.pzsp2back.orm.User;
import pl.pzsp2back.services.UserService;

@RestController()
@AllArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;
    private final DtoMapper dtoMapper;

    @GetMapping("/{login}")
    public ResponseEntity<?> findUserByLogin(@AuthenticationPrincipal User requesterUser, @PathVariable("login") String login) {
        log.info("Fetch user %s");
        if (login.isEmpty()) {
            return ResponseEntity.badRequest().body("Login not specified");
        }
        try {
            UserDto userDto;
            if ((requesterUser.getIfAdmin() && userService.ifSameGroup(requesterUser.getLogin(), login)) || requesterUser.getLogin().equals(login)) {
                userDto = dtoMapper.toDto(userService.getUser(login));
                return ResponseEntity.ok(userDto);
            }
            else {
                return new ResponseEntity<>("Action forbidden for this user.", HttpStatus.FORBIDDEN);
            }
        } catch (UserServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{login}")
    public ResponseEntity<?> putUser(@AuthenticationPrincipal User requesterUser, @Valid @RequestBody UserDto updateUser,  @PathVariable("login") String login) {
        try {
            UserDto updatedUserDto;
            if (requesterUser.getIfAdmin() && userService.ifSameGroup(requesterUser.getLogin(), login)) {
                updatedUserDto = dtoMapper.toDto(userService.updateUser(login, updateUser));
                return ResponseEntity.ok(updatedUserDto);
            } else {
                return new ResponseEntity<>("Action forbidden for this user.", HttpStatus.FORBIDDEN);
            }
        } catch (UserServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{login}")
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal User requesterUser, @PathVariable("login") String login) {
        try {
            UserDto deletedUserDto;
            if (requesterUser.getIfAdmin() && userService.ifSameGroup(requesterUser.getLogin(), login)) {
                deletedUserDto = dtoMapper.toDto(userService.deleteUser(login));
                return ResponseEntity.ok(deletedUserDto);
            } else {
                return new ResponseEntity<>("Action forbidden for this user.", HttpStatus.FORBIDDEN);
            }
        } catch (UserServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
