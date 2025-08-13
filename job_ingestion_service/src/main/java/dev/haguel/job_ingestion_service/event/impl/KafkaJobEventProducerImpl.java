package dev.haguel.job_ingestion_service.event.impl;

import dev.haguel.job_ingestion_service.event.JobEventProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaJobEventProducerImpl implements JobEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String jobSubmittedTopic;

    public KafkaJobEventProducerImpl(KafkaTemplate<String, String> kafkaTemplate,
                                 @Value("${kafka.topics.job-submitted}") String jobSubmittedTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.jobSubmittedTopic = jobSubmittedTopic;
    }

    @Override
    public void sendJobSubmittedEvent(String jobId) {
        kafkaTemplate.send(jobSubmittedTopic, jobId);
    }
}
