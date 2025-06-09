package com.example.actuator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootActuatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootActuatorApplication.class, args);
        System.out.println("http://localhost:8000/actuator/prometheus");
        System.out.println("http://localhost:8000/actuator/health");
        System.out.println("http://localhost:8080/actuator/info");
        System.out.println("http://localhost:8000/actuator/beans");
        System.out.println("http://localhost:8000/actuator/metrics");
        System.out.println("http://localhost:8000/actuator/env");
    }

}
