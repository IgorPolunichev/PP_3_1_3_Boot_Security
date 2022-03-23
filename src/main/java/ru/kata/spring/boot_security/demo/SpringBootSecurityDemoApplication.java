package ru.kata.spring.boot_security.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.User;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;

@SpringBootApplication
public class SpringBootSecurityDemoApplication {

	public static void main(String[] args) {


		SpringApplication.run(SpringBootSecurityDemoApplication.class, args);

	}

}
