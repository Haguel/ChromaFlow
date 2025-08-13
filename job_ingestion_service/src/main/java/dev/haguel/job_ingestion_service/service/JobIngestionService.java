package dev.haguel.job_ingestion_service.service;

import dev.haguel.model.JobIdDTO;
import dev.haguel.model.RecipeDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class JobIngestionService {
    public JobIdDTO ingestJob(RecipeDTO recipeDTO, MultipartFile mediaFile) {
        JobIdDTO jobIdDTO = new JobIdDTO();
        jobIdDTO.setJobId(UUID.randomUUID().toString());

        return jobIdDTO;
    }
}
