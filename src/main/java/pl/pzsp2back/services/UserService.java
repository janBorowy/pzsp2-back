package pl.pzsp2back.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pzsp2back.dto.UserDto;
import pl.pzsp2back.exceptions.UserServiceException;
import pl.pzsp2back.orm.User;
import pl.pzsp2back.orm.UserRepository;

import java.util.List;

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

    public User updateUser(String login, UserDto newUser) {
        var user = findUserByLogin(login);
        user.setHashedPassword(newUser.password());
        return userRepository.save(user);
    }

    public User deleteUser(String login) {
        var user = findUserByLogin(login);
        userRepository.delete(user);
        return user;
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


    //+ balance means adding to user balance and -balance is substracting from acutal balance
    public User addUserBalance(String login, Integer balance) {
        User user = findUserByLogin(login);
        user.setBalance(user.getBalance() + balance);
        return userRepository.save(user);

    }


    public boolean ifSameGroup(String login1, String login2) {
        User user1 = getUser(login1);
        List<User> usersList = user1.getGroup().getUsersList();
        for(User u : usersList) {
            if (u.getLogin().equals(login2))
                return true;
        }
        return false;
    }




}
