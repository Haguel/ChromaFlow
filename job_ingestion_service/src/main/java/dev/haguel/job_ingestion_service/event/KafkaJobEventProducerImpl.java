package dev.haguel.job_ingestion_service.event;

import org.springframework.stereotype.Component;

@Component
public class KafkaJobEventProducerImpl implements JobEventProducer{
    @Override
    public void sendJobSubmittedEvent(String jobId) {

    }
}
