package Entity;

import java.io.Serializable;

public class User {

    private int id;
    private String login;
    private String password;
    private String name;
    private String lastName;
    private String number;

    public User(String login, String password, String name, String lastName, String number) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
        this.number = number;
    }

    public User(int id, String name, String lastName, String number) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
