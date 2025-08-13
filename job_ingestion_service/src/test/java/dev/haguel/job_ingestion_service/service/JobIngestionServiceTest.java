package dev.haguel.job_ingestion_service.service;

import dev.haguel.job_ingestion_service.event.JobEventProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JobIngestionServiceTest {
    @Mock
    private JobEventProducer jobEventProducer;

    @InjectMocks
    private JobIngestionService jobIngestionService;

    @Test
    void whenIngestJob_thenSendJobSubmittedEvent() {
        jobIngestionService.ingestJob(null, null);

        verify(jobEventProducer).sendJobSubmittedEvent(anyString());
    }
}