package dev.haguel.media_processing_worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class MediaProcessingWorkerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MediaProcessingWorkerApplication.class, args);
    }

}
