package com.wipro.its;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class InterviewTrackingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterviewTrackingSystemApplication.class, args);
    }
}
