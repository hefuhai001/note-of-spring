package com.example.demo.controller;

import com.example.demo.base.Response;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public Response<?> getAllUsers() {
        List<User> list = userService.getAllUsers();
        return Response.ok(list);
    }

    @GetMapping("/{id}")
    public Response<?> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return Response.ok(user);
    }

    @PostMapping
    public Response<?> saveUser(@RequestBody User user) {
        userService.saveUser(user);
        return Response.ok(user);
    }

    @DeleteMapping("/{id}")
    public Response<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Response.ok(1);
    }
}