package dev.haguel.job_ingestion_service.service;

import dev.haguel.job_ingestion_service.event.JobEventProducer;
import dev.haguel.model.JobIdDTO;
import dev.haguel.model.RecipeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobIngestionService {
    private final JobEventProducer jobEventProducer;

    public JobIdDTO ingestJob(RecipeDTO recipeDTO, MultipartFile mediaFile) {
        JobIdDTO jobIdDTO = new JobIdDTO();
        jobIdDTO.setJobId(UUID.randomUUID().toString());

        jobEventProducer.sendJobSubmittedEvent(jobIdDTO.getJobId());

        return jobIdDTO;
    }
}
