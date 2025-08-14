package dev.haguel.job_ingestion_service.aop;

import dev.haguel.model.RecipeDTO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Aspect
@Component
public class RequestValidationAspect {
    @Around("@annotation(dev.haguel.job_ingestion_service.aop.annotation.ValidateMediaProcessingRequest)")
    public Object validateRequest(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        RecipeDTO recipeDTO = null;
        MultipartFile mediaFile = null;

        for (Object arg : proceedingJoinPoint.getArgs()) {
            if (arg instanceof RecipeDTO) {
                recipeDTO = (RecipeDTO) arg;
            } else if (arg instanceof MultipartFile) {
                mediaFile = (MultipartFile) arg;
            }
        }

        if (mediaFile == null || mediaFile.isEmpty()) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(400));
        }
        if (recipeDTO == null) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(400));
        }

        return proceedingJoinPoint.proceed();
    }
}
