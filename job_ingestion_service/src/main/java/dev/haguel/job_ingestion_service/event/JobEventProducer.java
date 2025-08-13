package dev.haguel.job_ingestion_service.event;

public interface JobEventProducer {
    void sendJobSubmittedEvent(String jobId);
}
