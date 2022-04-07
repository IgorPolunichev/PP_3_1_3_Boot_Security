package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.services.MyUserService;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    public MyUserService myUserService;

    @GetMapping
    public String showUserData( Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        user.setPassword(myUserService.decoding(user.getPassword()));
        model.addAttribute("userD",user);
        model.addAttribute("authUserName", user.getUserName());
        StringBuilder sb = new StringBuilder();
        for (Role r : user.getRoles()) {
            if (r.getRole().equals("ROLE_ADMIN")) {
                sb.append("ADMIN ");
            }
            if (r.getRole().equals("ROLE_USER")) {
                sb.append("USER ");
            }
        }
        model.addAttribute("authUserRole", sb.toString());
        return "userData";


    }
}
