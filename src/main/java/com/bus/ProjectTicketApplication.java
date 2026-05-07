package com.bus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProjectTicketApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectTicketApplication.class, args);
    }

}
