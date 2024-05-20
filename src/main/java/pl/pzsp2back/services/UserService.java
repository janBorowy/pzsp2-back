package pl.pzsp2back.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pzsp2back.dto.UserShortDto;
import pl.pzsp2back.exceptions.UserServiceException;
import pl.pzsp2back.orm.Group;
import pl.pzsp2back.orm.User;
import pl.pzsp2back.orm.UserRepository;

import java.util.List;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class UserService {


    private final UserRepository userRepository;

    public User getUser(String login) {
        return findUserByLogin(login);
    }

    public List<User> getUsers(List<String> logins) {
        return findUsersByLogin(logins);
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

    private List<User> findUsersByLogin(List<String> logins) {
        List<User> users = (List<User>) userRepository.findAllById(logins);
        if(users.isEmpty())
        {
            throw new UserServiceException("Users not found");
        }
        return users;
    }

    public static UserShortDto mapToUserShortDto(User user) {
        return new UserShortDto(user.getLogin(), user.getName(), user.getSurname());
    }


}
