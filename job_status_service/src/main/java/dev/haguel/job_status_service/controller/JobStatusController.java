package dev.haguel.job_status_service.controller;

import dev.haguel.api.JobStatusApi;
import dev.haguel.model.JobDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobStatusController implements JobStatusApi {
    @Override
    public ResponseEntity<JobDTO> getJobById(String jobId) {
        return JobStatusApi.super.getJobById(jobId);
    }
}
