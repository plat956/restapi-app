package com.epam.esm.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("execution(* com.epam.esm.controller.*.*(..))")
    public void restControllerAccess() {
    }

    @Pointcut("execution(* com.epam.esm.service.*.*(..))")
    public void serviceExceptions() {
    }

    @Before("restControllerAccess()")
    public void restBefore(JoinPoint joinPoint) {
        logger.debug("Target class: " + joinPoint.getTarget().getClass().getSimpleName());
        logger.debug("Entering in Method: " + joinPoint.getSignature().getName());
        logger.debug("Arguments: " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterThrowing(value = "serviceExceptions()", throwing="ex")
    public void serviceAfterThrowing(JoinPoint joinPoint, Exception ex) {
        logger.error("Thrown {} - \"{}\" from {} -> {} method",
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName());
    }
}
