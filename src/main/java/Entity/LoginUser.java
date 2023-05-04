package Entity;

import Entity.User;
import sql.UserRepository;

public class LoginUser {
    UserRepository userRepository;
    User user;
    public LoginUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User signIn(String login, String password){
        user = userRepository.findByUsername(login, password);

        if(user == null) {
            System.out.println("\nТакого користувача немає!");
            return null;
        } else {
            return user;
        }
    }

    public User signUp(String login, String password, String name, String lastName, String number){
        return userRepository.save(login, password, name, lastName, number);
    }
}
