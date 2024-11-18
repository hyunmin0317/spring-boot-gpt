package com.hyunmin.gpt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringBootGptApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootGptApplication.class, args);
    }
}