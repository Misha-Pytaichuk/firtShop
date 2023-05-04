package sql;

import Entity.User;

import java.util.List;

public interface UserRepository {
    User save(String login, String password, String name, String lastName, String number);
    User findByUsername(String username, String password);
    List<User> findAll();
    void deleteByUsername(String username);
}
