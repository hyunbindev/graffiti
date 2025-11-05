package com.hyunbindev.graffiti.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.hyunbindev.graffiti.repository.jpa")
public class JpaConfig {

}
