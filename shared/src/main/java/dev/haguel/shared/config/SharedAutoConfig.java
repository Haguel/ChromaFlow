package dev.haguel.shared.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "dev.haguel.shared.aop")
public class SharedAutoConfig {
}
