package pl.pzsp2back.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.pzsp2back.dto.SignUpDto;
import pl.pzsp2back.orm.User;
import pl.pzsp2back.orm.UserRepository;

@Service
@AllArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public UserDetails signUp(SignUpDto dto) {
        if (userRepository.findById(dto.login()).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        String hashedPassword = new BCryptPasswordEncoder().encode(dto.password());
        User newUser = new User(dto.login(), hashedPassword, false, dto.email(), dto.name(), dto.surname(), dto.groupId());
        return userRepository.save(newUser);
    }
}
