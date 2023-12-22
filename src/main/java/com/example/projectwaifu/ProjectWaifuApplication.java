package com.example.projectwaifu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ProjectWaifuApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectWaifuApplication.class, args);
    }

}
