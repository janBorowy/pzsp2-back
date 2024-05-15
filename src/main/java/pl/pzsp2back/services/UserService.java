package pl.pzsp2back.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pzsp2back.exceptions.UserServiceException;
import pl.pzsp2back.orm.User;
import pl.pzsp2back.orm.UserRepository;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUser(String login) {
        return findUserByLogin(login);
    }

    public User updateUser(String login, User newUser) {
        var user = findUserByLogin(login);
        newUser.setHashedPassword(user.getHashedPassword());
        userRepository.save(newUser);
        return newUser;
    }

    public User deleteUser(String login) {
        var user = findUserByLogin(login);
        userRepository.delete(user);
        return user;
    }

    private User findUserByLogin(String login) {
        return userRepository.findById(login)
                .orElseThrow(() -> new UserServiceException("User not found"));
    }
}
