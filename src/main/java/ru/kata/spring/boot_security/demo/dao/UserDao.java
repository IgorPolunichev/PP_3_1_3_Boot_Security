package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class UserDao implements Dao {
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public User findByUserName(String userName) {
        return (User) entityManager
                .createQuery("SELECT u FROM User u WHERE u.userName=:custName")
                .setParameter("custName", userName)
                .getSingleResult();
    }

    @Transactional
    @Override
    public void addUser(User user) {
        entityManager.persist(user);
    }

    @Transactional
    @Override
    public void addRole(Role role) {
        if (entityManager.find(Role.class, role.getId()) == null) {
            entityManager.persist(role);
        }
    }


    @Override
    public Role getRole(Long role) {
        return entityManager.find(Role.class, role);
    }

    @Override
    public User getUserById(Long id) {
        User user = entityManager.find(User.class, id);
        return user;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> listUser() {
        return entityManager.createQuery("select u from User u", User.class).getResultList();
    }

    @Override
    @Transactional
    public void editUser(User user) {
        entityManager.merge(user);

    }

    @Override
    @Transactional
    public void removeUser(Long id) {
        User user = getUserById(id);
        entityManager.remove(user);

    }


}
