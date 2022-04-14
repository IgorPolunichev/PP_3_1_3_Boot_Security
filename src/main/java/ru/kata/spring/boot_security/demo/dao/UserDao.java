package ru.kata.spring.boot_security.demo.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.BeanCreationException;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;


import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
        String sql = String.format("select u from User u where u.userName = '%s'", user.getUserName());
        try {
            User user1 = (User) entityManager.createQuery(sql).getSingleResult();

        }catch (BeanCreationException | NoResultException ignore){
                entityManager.persist(user);
        }
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
