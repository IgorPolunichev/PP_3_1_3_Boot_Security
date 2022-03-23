package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.services.MyUserService;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {
    @Autowired
    private MyUserService myUserService;

    @GetMapping
    public String showUsers(ModelMap model) {
        model.addAttribute("users", myUserService.listUser());
        return "adminData";
    }

    @GetMapping(value = "/addUser")
    public String addUser(Model model) {
        User user = new User();
        Role role = new Role();
        model.addAttribute("newUser", user);
        model.addAttribute("roles", user.getRoles());
        model.addAttribute("userRole", role);
        return "newUser";
    }

    @PostMapping(value = "/addUser")
    public String createUser(@ModelAttribute("newUser") User user) {
        User user1 = new User();
        user1.setPassword(user.getPassword());
        user1.setUserName(user.getUserName());
        Set<Role> roles = new HashSet<>();

        for (Role role : user.getRoles()) {
            if (role.getRole().equals("ROLE_ADMIN")) {
                roles.add(myUserService.getRole(2L));
            }
            if (role.getRole().equals("ROLE_USER")) {
                roles.add(myUserService.getRole(1L));
            }
        }
        user1.setRole(roles);

        myUserService.addUser(user1);
        return "redirect:/admin";
    }

    @DeleteMapping(value = "/remove/{id}")
    public String deleteUser(@PathVariable(name = "id") Long id) {
        myUserService.removeUser(id);
        return "redirect:/admin ";
    }


    @GetMapping(value = "/edit/{id}")
    public String editUser(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("user", myUserService.getUserById(id));
        model.addAttribute("ur", new Role());
        return "editUser";
    }

    @PatchMapping("/edit/{id}")
    public String update(@ModelAttribute("user") User user,
                         @PathVariable("id") Long id) {
        Set<Role> role = myUserService.getUserById(id).getRoles();
        user.setRole(role);
        myUserService.editUser(user);
        return "redirect:/admin ";
    }

    @DeleteMapping(value = "/removeRole/{user_id}/{role_id}")
    public String deleteUserRole(@PathVariable(name = "user_id") Long userId,
                                 @PathVariable(name = "role_id") Long roleId) {
        Set<Role> userRole = myUserService.getUserById(userId).getRoles();
        userRole = userRole.stream().filter(e -> e.getId() != roleId).collect(Collectors.toSet());
        User user = myUserService.getUserById(userId);
        user.setRole(userRole);
        myUserService.editUser(user);
        return String.format("redirect:/admin/edit/%d", userId);
    }

    @PatchMapping("/addRole/{user_id}")
    public String addRole(@ModelAttribute("ur") Role role, @PathVariable("user_id") Long id) {
        if (role.getRole().equals("ROLE_USER")) {
            User user = myUserService.getUserById(id);
            user.addRole(myUserService.getRole(1L));
            myUserService.editUser(user);
        }
        if (role.getRole().equals("ROLE_ADMIN")) {
            User user = myUserService.getUserById(id);
            user.addRole(myUserService.getRole(2L));
            myUserService.editUser(user);

        }
        return "redirect:/admin";
    }

}
