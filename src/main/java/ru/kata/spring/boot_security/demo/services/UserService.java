package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.Dao;
import ru.kata.spring.boot_security.demo.dao.DaoRole;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;


import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService, MyUserService {

    private Dao userDao;
    private DaoRole roleDao;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(Dao userDao, DaoRole roleDao, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.findByUserName(username);
    }


    public void addUsers() {
        Role user = new Role("ROLE_USER");
        user.setId(1L);
        Role admin = new Role("ROLE_ADMIN");
        admin.setId(2L);
        roleDao.addRole(user);
        roleDao.addRole(admin);
        User user1 = new User(bCryptPasswordEncoder.encode("pass1"), "user1");
        Set<Role> user1Rols = new HashSet<>();
        user1Rols.add(roleDao.getRole(2L));
        user1.setRole(user1Rols);
        userDao.addUser(user1);


    }

    @Override
    public User getUserById(Long id) {
        return userDao.getUserById(id);
    }

    @Override
    public List<User> listUser() {
        return userDao.listUser();
    }

    @Override
    public void addUser(User user) {

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userDao.addUser(user);

    }

    @Override
    public void editUser(User user) {
        if (!bCryptPasswordEncoder.matches(user.getPassword(), getUserById(user.getId()).getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userDao.editUser(user);

    }

    @Override
    public void removeUser(Long id) {
        userDao.removeUser(id);

    }

    @Override
    public Role getRole(Long roleName) {
        return roleDao.getRole(roleName);
    }

    @Override
    public String decoding(String codingPass) {
        BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(bc.encode(codingPass));
    }

    @Override
    public String[] arrayRoles() {
        return roleDao.arrayRole();
    }

}
