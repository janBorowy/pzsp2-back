package pl.pzsp2back.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.pzsp2back.dto.SignInDto;
import pl.pzsp2back.dto.SignInResponseDto;
import pl.pzsp2back.dto.SignUpDto;
import pl.pzsp2back.dto.TokenDto;
import pl.pzsp2back.exceptions.AuthException;
import pl.pzsp2back.orm.User;
import pl.pzsp2back.orm.UserRepository;
import pl.pzsp2back.security.AuthService;
import pl.pzsp2back.security.TokenProvider;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {
    private final UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private AuthService service;
    private TokenProvider tokenService;

    @PostMapping("/signup")
    public ResponseEntity<?> singUp(@RequestBody SignUpDto dto) {
        log.info("Signing up user %s".formatted(dto));
        service.signUp(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInDto dto) {
        try {
            log.info("Signing in user %s".formatted(dto));
            var usernamePassword = new UsernamePasswordAuthenticationToken(dto.login(), dto.password());
            var authUser = authenticationManager.authenticate(usernamePassword);
            var accessToken = tokenService.generateAccessToken((User) authUser.getPrincipal());
            log.info("Successfully signed in user %s".formatted(dto));
            return ResponseEntity.ok(new TokenDto(accessToken, dto.login()));
        } catch (AuthenticationException e) {
            log.info("Failed authorization for user %s".formatted(dto));
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

}
