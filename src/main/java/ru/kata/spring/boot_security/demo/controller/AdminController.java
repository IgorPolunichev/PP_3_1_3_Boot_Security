package ru.kata.spring.boot_security.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import com.mysql.cj.xdevapi.JsonArray;
import com.mysql.cj.xdevapi.JsonValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.services.MyUserService;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {
    @Autowired
    private MyUserService myUserService;

    @GetMapping
    public String showUsers(ModelMap model) {
        User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("users", myUserService.listUser());
        model.addAttribute("authUserName", myUserService.getUserById(auth.getId()).getUserName());
        StringBuilder sb = new StringBuilder();
        for (Role r : myUserService.getUserById(auth.getId()).getRoles()) {
            if (r.getRole().equals("ROLE_ADMIN")) {
                sb.append("ADMIN ");
            }
            if (r.getRole().equals("ROLE_USER")) {
                sb.append("USER ");
            }
        }
        model.addAttribute("authUserRole", sb.toString());
        model.addAttribute("authUser", auth);
        return "test";
    }


    @PostMapping(value = "/addUser")
    public String createUser(User user, HttpServletRequest request) {
        Set<Role> roles = new HashSet<>();
        String[] userRoles = request.getParameterValues("role1");
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

    @RequestMapping("/getUserById/{id}")
    @ResponseBody
    public HashMap<String, String[]> getUserById(@PathVariable("id") Long id) throws JsonProcessingException {
        User user = myUserService.getUserById(id);
        HashMap<String, String[]>  test= new HashMap<>();
        List<String> t = user.getRoles().stream().map(Role::getRole).toList();
        test.put("userId", new String[]{user.getId().toString()});
        test.put("userName", new String[]{user.getUserName()});
        test.put("userAge", new String[]{user.getAge().toString()});
        test.put("userRoles", t.toArray(new String[0]));
        return test;
    }

    @RequestMapping("/update")

    public String update(User user
            , HttpServletRequest request) {
        String[] userRoles = request.getParameterValues("role1");
        Set<Role> role = new HashSet<>();
        for (String s : userRoles) {
            if (s.equals("1")) {
                role.add(myUserService.getRole(1L));
            }
            if (s.equals("2")) {
                role.add(myUserService.getRole(2L));
            }
        }
        if (user.getPassword().equals("")) {
            user.setPassword(myUserService.getUserById(user.getId()).getPassword());
        }
        user.setRole(role);
        myUserService.editUser(user);
        return "redirect:/admin ";
    }

}
