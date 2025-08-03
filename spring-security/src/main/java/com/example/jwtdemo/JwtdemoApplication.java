package com.example.jwtdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class JwtdemoApplication {

    private static PasswordEncoder encoder;

    public JwtdemoApplication(PasswordEncoder encoder) {
        JwtdemoApplication.encoder = encoder;
    }

    public static void main(String[] args) {
        SpringApplication.run(JwtdemoApplication.class, args);

        System.out.println(encoder.encode("123456"));

        System.out.println(encoder.matches("123456",
                "$2a$10$HgeBKKmhSVYsTMlw/vjF0enEN4v0dUdKlm7.fP.gFlusNuDCBj4YK"));

    }

}
