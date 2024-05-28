package pl.pzsp2back.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pzsp2back.dto.UserDto;
import pl.pzsp2back.dto.UserShortDto;
import pl.pzsp2back.exceptions.UserServiceException;
import pl.pzsp2back.orm.User;
import pl.pzsp2back.orm.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {


    private final UserRepository userRepository;

    public UserDto getUser(String login) {
        return mapToUserDto(findUserByLogin(login));
    }

    public List<UserDto> getUsers(List<String> logins) {
        return findUsersByLogin(logins).stream().map(u -> mapToUserDto(u)).collect(Collectors.toList());
    }

    public UserDto updateUser(String login, User newUser) {
        var user = findUserByLogin(login);
        newUser.setHashedPassword(user.getHashedPassword());
        userRepository.save(newUser);
        return mapToUserDto(newUser);
    }

    public UserDto deleteUser(String login) {
        var user = findUserByLogin(login);
        userRepository.delete(user);
        return mapToUserDto(user);
    }

    public User findUserByLogin(String login) {
        return userRepository.findById(login)
                .orElseThrow(() -> new UserServiceException("User not found"));
    }

    public List<User> findUsersByLogin(List<String> logins) {
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

    public static UserDto mapToUserDto(User user) {
        return new UserDto(user.getLogin(), user.getPassword(), user.getIfAdmin(), user.getBalance(), user.getEmail(), user.getName(), user.getSurname(), user.getGroup().getId());
    }


}
