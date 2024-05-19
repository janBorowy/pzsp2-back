package pl.pzsp2back.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.pzsp2back.dto.SignUpDto;
import pl.pzsp2back.exceptions.UserAlreadyExistsException;
import pl.pzsp2back.exceptions.UserServiceException;
import pl.pzsp2back.orm.GroupRepository;
import pl.pzsp2back.orm.User;
import pl.pzsp2back.orm.UserRepository;

@Service
@AllArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public UserDetails signUp(SignUpDto dto) {
        if (userRepository.findById(dto.login()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        String hashedPassword = new BCryptPasswordEncoder().encode(dto.password());
        var group = groupRepository.findById(dto.groupId())
                .orElseThrow(() -> new UserServiceException("Group with given id does not exist"));
        User newUser = new User(dto.login(), hashedPassword, false, dto.initialBalance(), dto.email(), dto.name(), dto.surname(), group, null);
        return userRepository.save(newUser);
    }
}
