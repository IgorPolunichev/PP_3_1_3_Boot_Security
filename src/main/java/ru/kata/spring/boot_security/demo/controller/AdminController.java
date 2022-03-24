package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.services.MyUserService;

import javax.servlet.http.HttpServletRequest;
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
        model.addAttribute("newUser", user);
        return "newUser";
    }

    @PostMapping(value = "/addUser")
    public String createUser(@ModelAttribute("newUser") User user, HttpServletRequest request) {
        Set<Role> roles = new HashSet<>();
        String[] userRoles = request.getParameterValues("role1");
//        User user1 = new User();
//        user1.setPassword(user.getPassword());
//        user1.setUserName(user.getUserName());
        for (String roleId : userRoles) {
            if (Long.parseLong(roleId) == 2L) {
                roles.add(myUserService.getRole(2L));
            }
            if (Long.parseLong(roleId) == 1L) {
                roles.add(myUserService.getRole(1L));
            }
        }
        user.setRole(roles);
        myUserService.addUser(user);
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
    public String addRole(HttpServletRequest request, @PathVariable("user_id") Long userId) {
        String[] userRoles = request.getParameterValues("addRole");
        if (userRoles[0].equals("1")) {
            User user = myUserService.getUserById(userId);
            user.addRole(myUserService.getRole(1L));
            myUserService.editUser(user);
        }else if (userRoles[0].equals("2")) {
            User user = myUserService.getUserById(userId);
            user.addRole(myUserService.getRole(2L));
            myUserService.editUser(user);

        }
        return String.format("redirect:/admin/edit/%d", userId);
    }

}
