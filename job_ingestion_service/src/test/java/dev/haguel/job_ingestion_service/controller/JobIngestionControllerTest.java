package dev.haguel.job_ingestion_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.haguel.job_ingestion_service.service.JobIngestionService;
import dev.haguel.model.JobIdDTO;
import dev.haguel.model.RecipeDTO;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;


import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class JobIngestionControllerTest {
    @Nested
    @SpringBootTest
    @AutoConfigureMockMvc
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    class JobIngestionControllerMockMvcTest {
        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private JobIngestionService jobIngestionService;

        MockMultipartFile multipartMediaFile = new MockMultipartFile(
                "mediaFile",
                "test.img",
                MediaType.IMAGE_JPEG_VALUE,
                "Hello World".getBytes()
        );

        RecipeDTO recipeDTO = new RecipeDTO();

        @BeforeEach
        void setup() {
            when(jobIngestionService
                    .ingestJob(ArgumentMatchers.any(RecipeDTO.class), ArgumentMatchers.any(MultipartFile.class)))
                    .thenReturn(getJobIdDTOWithRandomUUID());
        }

        private MockMultipartFile getMultipartRecipeJson() throws JsonProcessingException {
            byte[] recipeBytes = objectMapper.writeValueAsBytes(recipeDTO);
            return new MockMultipartFile(
                    "recipe",
                    "",
                    MediaType.APPLICATION_JSON_VALUE,
                    recipeBytes
            );
        }

        private JobIdDTO getJobIdDTOWithRandomUUID() {
            JobIdDTO jobIdDTO = new JobIdDTO();
            jobIdDTO.setJobId(UUID.randomUUID().toString());

            return jobIdDTO;
        }

        @Test
        void whenSubmitMediaProcessingTaskWithValidData_thenReturn202() throws Exception {
            mockMvc.perform(multipart("/api/v1/jobs")
                    .file(multipartMediaFile)
                    .file(getMultipartRecipeJson()))
                    .andExpect(status().isAccepted())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.jobId").exists())
                    .andExpect(jsonPath("$.jobId",is(not(emptyString()))));

            verify(jobIngestionService)
                    .ingestJob(ArgumentMatchers.any(RecipeDTO.class), ArgumentMatchers.any(MultipartFile.class));
        }

        @Test
        void whenSubmitMediaProcessingTaskWithoutFile_thenReturn400() throws Exception {
            mockMvc.perform(multipart("/api/v1/jobs")
                            .file(getMultipartRecipeJson()))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void whenSubmitMediaProcessingTaskWithoutRecipeDTO_thenReturn400() throws Exception {
            mockMvc.perform(multipart("/api/v1/jobs")
                            .file(multipartMediaFile))
                    .andExpect(status().isBadRequest());
        }
    }

    // MockMvc can't test request size, that's why made RestAssured test
    @Nested
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    class JobIngestionControllerRestAssuredTest {
        @LocalServerPort
        private int port;

        @Value("${spring.servlet.multipart.max-file-size}")
        private String maxFileSize;


        @BeforeEach
        public void setup() {
            RestAssured.port = port;
        }

        private int getMaxFileSizeInBytes() {
            String value = maxFileSize.toUpperCase().replace(" ", "");
            if (value.endsWith("MB")) {
                return Integer.parseInt(value.replace("MB", "")) * 1024 * 1024;
            } else if (value.endsWith("KB")) {
                return Integer.parseInt(value.replace("KB", "")) * 1024;
            } else if (value.endsWith("B")) {
                return Integer.parseInt(value.replace("B", ""));
            } else {
                throw new RuntimeException("Undefined format: " + value);
            }
        }

        private byte[] createAndGetBytesWithSize(int sizeInKbs) {
            int fileSize = sizeInKbs * 1024;
            return new byte[fileSize];
        }

        @Test
        void whenSubmitMediaProcessingTaskWithTooLargeFile_thenReturn413() {
            int fileSize = getMaxFileSizeInBytes() + 1;

            given()
                    .multiPart("mediaFile", "mediaFile", createAndGetBytesWithSize(fileSize))
                    .when()
                    .post("/jobs")
                    .then()
                    .statusCode(413);
        }
    }
}