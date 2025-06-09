package com.example.demo.initializer;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    public void init() {
        List<User> users = Arrays.asList(
                new User("hfh", "hfh@example.com"),
                new User("lm", "lm@example.com")
        );
        userRepository.saveAll(users);
    }

}
