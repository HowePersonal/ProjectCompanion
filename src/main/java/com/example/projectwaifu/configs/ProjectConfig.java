package com.example.projectwaifu.configs;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients
@ComponentScan(basePackages = {"com.example.projectwaifu"})
public class ProjectConfig {

}
