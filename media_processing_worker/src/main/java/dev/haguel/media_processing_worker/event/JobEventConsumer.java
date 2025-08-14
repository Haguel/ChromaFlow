package dev.haguel.media_processing_worker.event;

public interface JobEventConsumer {
    void consumeJobId(String jobId);
}
