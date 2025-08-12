package dev.haguel.job_ingestion_service.controller;

import dev.haguel.api.JobIngestionApi;
import dev.haguel.job_ingestion_service.aop.ValidateMediaProcessingRequest;
import dev.haguel.model.JobIdDTO;
import dev.haguel.model.RecipeDTO;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
public class JobIngestionController implements JobIngestionApi {
    @Override
    @ValidateMediaProcessingRequest
    public ResponseEntity<JobIdDTO> submitMediaProcessingTask(RecipeDTO recipeDTO, MultipartFile file) {
        JobIdDTO jobIdDTO = new JobIdDTO();
        jobIdDTO.setJobId(UUID.randomUUID().toString());

        return new ResponseEntity<>(jobIdDTO, HttpStatusCode.valueOf(202));
    }
}
