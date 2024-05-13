package pl.pzsp2back.controllers;

import lombok.AllArgsConstructor;
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
import pl.pzsp2back.dto.SignUpDto;
import pl.pzsp2back.dto.TokenDto;
import pl.pzsp2back.exceptions.AuthException;
import pl.pzsp2back.orm.User;
import pl.pzsp2back.security.AuthService;
import pl.pzsp2back.security.TokenProvider;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private AuthenticationManager authenticationManager;
    private AuthService service;
    private TokenProvider tokenService;

    @PostMapping("/signup")
    public ResponseEntity<?> singUp(@RequestBody SignUpDto dto) {
        service.signUp(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInDto dto) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(dto.login(), dto.password());
            var authUser = authenticationManager.authenticate(usernamePassword);
            var accessToken = tokenService.generateAccessToken((User) authUser.getPrincipal());
            return ResponseEntity.ok(new TokenDto(accessToken));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

}
