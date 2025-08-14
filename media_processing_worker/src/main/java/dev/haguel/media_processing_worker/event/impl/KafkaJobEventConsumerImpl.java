package dev.haguel.media_processing_worker.event.impl;

import dev.haguel.media_processing_worker.event.JobEventConsumer;
import dev.haguel.media_processing_worker.media_processing_strategy.MediaProcessingStrategy;
import dev.haguel.media_processing_worker.media_processing_strategy.impl.GrayscaleMediaProcessingStrategyImpl;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import service.StorageService;

import java.awt.image.BufferedImage;

@Component
public class KafkaJobEventConsumerImpl implements JobEventConsumer {
    MediaProcessingStrategy mediaProcessingStrategy;
    StorageService storageService;

    @KafkaListener(topics = "${kafka.topics.job-submitted}", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consumeJobId(String jobId) {
        BufferedImage bufferedImage = storageService.downloadImage(jobId);
        mediaProcessingStrategy = new GrayscaleMediaProcessingStrategyImpl();
        mediaProcessingStrategy.apply(bufferedImage);
    }
}
