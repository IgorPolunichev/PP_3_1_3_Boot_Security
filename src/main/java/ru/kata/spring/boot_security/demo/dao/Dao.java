package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface Dao {
    User findByUserName(String userName);

    void addUser(User user);
    User getUserById(Long id);
    List<User> listUser();
    void editUser (User user);
    void removeUser(Long id);


}
