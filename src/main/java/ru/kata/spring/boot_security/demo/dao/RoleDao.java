package ru.kata.spring.boot_security.demo.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class RoleDao implements DaoRole{
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public void addRole(Role role) {
        if (entityManager.find(Role.class, role.getId()) == null) {
            entityManager.persist(role);
        }
    }

    @Override
    public Role getRole(Long role) {
        SessionFactory sessionFactory =  entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Role role1 = session.get(Role.class,role);
        session.close();
        return role1;

//        return entityManager.find(Role.class, role);
    }

    @Override
    public String[] arrayRole() {

        List<String> test = entityManager.
                createQuery("select r from Role r", Role.class)
                .getResultList().stream().map(Role::getRole).toList();
        String[] roles = test.toArray(new String[test.size()]);

        return roles;
    }


}
