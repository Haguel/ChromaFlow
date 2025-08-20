package dev.haguel.job_ingestion_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class JobIngestionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobIngestionServiceApplication.class, args);
    }

}
