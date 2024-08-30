package com.example.photocontest.controllers.rest;

import com.example.photocontest.mappers.UserMapper;
import com.example.photocontest.models.Role;
import com.example.photocontest.models.User;
import com.example.photocontest.models.dto.UserDto;
import com.example.photocontest.services.EmailService;
import com.example.photocontest.services.contracts.UserService;
import  org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.security.Security;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public List<User> getAllUsers() {
      return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
       return userService.findUserById(id);
    }

    @PostMapping
    public User createUser(@RequestBody UserDto userDto) {
        User user = userService.createUser(userMapper.fromDto(userDto));
        emailService.sendSimpleEmail(user.getEmail(),user.getUsername());
        return user;
    }

    @PostMapping("/{id}/roles")
    public User setRole(@PathVariable int id, @RequestParam String role, Principal principal) {
        checkPermission(principal, "ADMIN");
        return userService.setRole(id, role);
    }

    @GetMapping("/{id}/roles")
    public List<Role> getUserRoles(@PathVariable int id) {
        return userService.findUserById(id).getRoles();
    }

    @DeleteMapping("/{id}/roles")
    public User removeRole(@PathVariable int id, @RequestParam String role, Principal principal) {
        checkPermission(principal, "ADMIN");
        return userService.removeRole(id, role);
    }

    private void checkPermission(Principal principal, String requestedRole) {
        User user = userService.findUserByUsername(principal.getName());
        for (Role role : user.getRoles()) {
            if (role.getName().name().equalsIgnoreCase(requestedRole)) {
                return;
            }
        }
        throw new SecurityException("You do not have permission to perform this operation");
    }

}
