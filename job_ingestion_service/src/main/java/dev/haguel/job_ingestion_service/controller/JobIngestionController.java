package dev.haguel.job_ingestion_service.controller;

import dev.haguel.api.JobIngestionApi;
import dev.haguel.job_ingestion_service.aop.ValidateMediaProcessingRequest;
import dev.haguel.job_ingestion_service.service.JobIngestionService;
import dev.haguel.model.JobIdDTO;
import dev.haguel.model.RecipeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class JobIngestionController implements JobIngestionApi {
    private final JobIngestionService jobIngestionService;

    @Override
    @ValidateMediaProcessingRequest
    public ResponseEntity<JobIdDTO> submitMediaProcessingTask(
            @RequestPart(value = "recipe") RecipeDTO recipeDTO,
            @RequestPart(value = "mediaFile") MultipartFile mediaFile) {
        JobIdDTO jobIdDTO = jobIngestionService.ingestJob(recipeDTO, mediaFile);

        return new ResponseEntity<>(jobIdDTO, HttpStatusCode.valueOf(202));
    }
}
