package com.peermentoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot Application Entry Point for Peer-to-Peer Academic Mentoring Hub.
 * 
 * Educational Explanation for Viva:
 * @SpringBootApplication enables three key features:
 * 1. @EnableAutoConfiguration: Configures Spring Boot automatically based on dependencies (Web, JPA, MySQL).
 * 2. @ComponentScan: Scans packages for @Controller, @Service, @Repository, and @Component beans.
 * 3. @SpringBootConfiguration: Indicates that this class provides Spring application context configuration.
 */
@SpringBootApplication
public class PeerMentoringApplication {

    public static void main(String[] args) {
        SpringApplication.run(PeerMentoringApplication.class, args);
        System.out.println("\n=======================================================");
        System.out.println("  Peer Mentoring & Doubt Clearing Hub Server Running! ");
        System.out.println("  Backend URL: http://localhost:8080");
        System.out.println("=======================================================\n");
    }
}
