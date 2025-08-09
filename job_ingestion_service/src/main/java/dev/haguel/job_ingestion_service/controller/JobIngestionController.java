package dev.haguel.job_ingestion_service.controller;

import dev.haguel.api.JobIngestionApi;
import dev.haguel.model.JobIdDTO;
import dev.haguel.model.RecipeDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class JobIngestionController implements JobIngestionApi {
    @Override
    public ResponseEntity<JobIdDTO> submitMediaProcessingTask(RecipeDTO recipe, MultipartFile file) {
        return JobIngestionApi.super.submitMediaProcessingTask(recipe, file);
    }
}
