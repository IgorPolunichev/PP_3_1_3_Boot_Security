package ru.kata.spring.boot_security.demo.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "table_roles")
public class Role implements GrantedAuthority {

    @Id
    private Long id;

    @Column(name = "role")
    private String role;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role"
            , joinColumns = @JoinColumn(name = "role_id")
            , inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users;


    public Role() {
    }

    public Role(String role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String getAuthority() {
        return role;
    }
}
