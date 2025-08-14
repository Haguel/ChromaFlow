package dev.haguel.media_processing_worker.event.impl;

import dev.haguel.media_processing_worker.event.JobEventConsumer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaJobEventConsumerImpl implements JobEventConsumer {
    @KafkaListener(topics = "${kafka.topics.job-submitted}", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consumeJobId(String jobId) {
        System.out.println("JOB ID: " + jobId);
    }
}
