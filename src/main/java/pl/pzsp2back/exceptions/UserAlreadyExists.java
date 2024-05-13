package pl.pzsp2back.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "User already exists")
public class UserAlreadyExists extends RuntimeException {
    public UserAlreadyExists() {
        super("User already exists");
    }
}
