package com.example.backend.aspects;

import com.example.backend.services.FshareService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class FshareServiceAspect {
    private final Logger logger = LoggerFactory.getLogger(FshareServiceAspect.class);
    @Autowired
    private HttpServletRequest request;

    @Before("execution(* com.example.backend.services.FshareService.*(..)) && target(fshareService)")
    public void aroundExecution(JoinPoint joinPoint, FshareService fshareService) {
        try {
            fshareService.setFshareData(request);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
    }
}
