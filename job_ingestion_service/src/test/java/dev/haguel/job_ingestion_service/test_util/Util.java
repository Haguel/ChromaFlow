package dev.haguel.job_ingestion_service.test_util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.haguel.model.JobIdDTO;
import dev.haguel.model.RecipeDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

@Component
public class Util {
    private static ObjectMapper objectMapper;

    @Autowired
    public Util(ObjectMapper objectMapper) {
        Util.objectMapper = objectMapper;
    }

    public static MockMultipartFile getBlankMockMultipartFileWithName(String name) {
        return new MockMultipartFile(
                name,
                "",
                MediaType.IMAGE_JPEG_VALUE,
                "0".getBytes()
        );
    }

    public static MockMultipartFile getBlankMockMultipartRecipeJsonWithName(String name) {
        try {
            RecipeDTO recipeDTO = new RecipeDTO();
            byte[] recipeBytes = objectMapper.writeValueAsBytes(recipeDTO);
            return new MockMultipartFile(
                    name,
                    "",
                    MediaType.APPLICATION_JSON_VALUE,
                    recipeBytes
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JobIdDTO getJobIdDTOWithRandomUUID() {
        JobIdDTO jobIdDTO = new JobIdDTO();
        jobIdDTO.setJobId(java.util.UUID.randomUUID().toString());

        return jobIdDTO;
    }
}
