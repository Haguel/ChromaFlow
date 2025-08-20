package dev.haguel.shared.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Pointcut("within(dev.haguel..*) " +
            "&& (@within(org.springframework.stereotype.Service) " +
            "|| @within(org.springframework.web.bind.annotation.RestController))")
    public void springBeanPointcut() { }

    @Around("springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if (log.isDebugEnabled()) {
            log.debug("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }
        try {
            long startTime = System.currentTimeMillis();
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            long executionTimeInMs = endTime - startTime;

            if (log.isDebugEnabled()) {
                log.debug("Exit: {}.{}() with result = {}. Execution time = {} ms", joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(), result, executionTimeInMs);
            }

            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());

            throw e;
        }
    }
}