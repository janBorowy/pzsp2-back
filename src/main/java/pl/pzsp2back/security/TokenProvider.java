package pl.pzsp2back.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.pzsp2back.orm.User;

@Service
public class TokenProvider {

    @Value("${security.jwt.token.expiration-time-in-hours}")
    private int EXPIRATION_TIME_IN_HOURS;

    private final String JWT_SECRET;

    public TokenProvider() {
        JWT_SECRET = RandomStringUtils.randomAlphabetic(32);
    }

    public String generateAccessToken(User user) {
        return JWT.create()
                .withSubject(user.getLogin())
                .withClaim("login", user.getLogin())
                .withExpiresAt(getAccessExpirationDate())
                .sign(Algorithm.HMAC256(JWT_SECRET));
    }

    public String validateAccessToken(String token) {
        return JWT.require(Algorithm.HMAC256(JWT_SECRET))
                .build()
                .verify(token)
                .getSubject();
    }

    public Date getAccessExpirationDate() {
        return Date.from(LocalDateTime.now().plusHours(EXPIRATION_TIME_IN_HOURS).atZone(ZoneId.systemDefault()).toInstant());
    }
}
