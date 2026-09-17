package com.example.hfh;

import com.example.hfh.entity.User;
import com.example.hfh.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserMapper userMapper;

    private User testUser;

    @BeforeEach
    void setUp() {
        userMapper.delete(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>());
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPhone("13800138000");
    }

    @Test
    void testCreateUser() {
        ResponseEntity<User> response = restTemplate.postForEntity("/api/users", testUser, User.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals("testuser", response.getBody().getUsername());
    }

    @Test
    void testGetUserById() {
        userMapper.insert(testUser);

        ResponseEntity<User> response = restTemplate.getForEntity("/api/users/{id}", User.class, testUser.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("testuser", response.getBody().getUsername());
    }

    @Test
    void testListUsers() {
        userMapper.insert(testUser);

        User anotherUser = new User();
        anotherUser.setUsername("another");
        anotherUser.setEmail("another@test.com");
        userMapper.insert(anotherUser);

        ResponseEntity<User[]> response = restTemplate.getForEntity("/api/users", User[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length >= 2);
    }

    @Test
    void testUpdateUser() {
        userMapper.insert(testUser);
        testUser.setEmail("updated@example.com");

        HttpEntity<User> requestEntity = new HttpEntity<>(testUser);
        ResponseEntity<User> response = restTemplate.exchange(
                "/api/users/" + testUser.getId(),
                HttpMethod.PUT,
                requestEntity,
                User.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("updated@example.com", response.getBody().getEmail());
    }
}
