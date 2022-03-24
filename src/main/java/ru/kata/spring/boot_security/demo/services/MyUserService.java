package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface MyUserService {
    void addUsers();
    User getUserById(Long id);
    List<User> listUser();
    void addUser(User user);
    void editUser (User user);
    void removeUser(Long id);
    Role getRole (Long roleName);
    String decoding(String codePass);
    String[] arrayRoles();



}
