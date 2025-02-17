package de.telran.gartenshop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.logging.Logger;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Around("@annotation(LogAnnotation)")
    public Object aroundCallAt(ProceedingJoinPoint pjp) throws Throwable {
        return logInformation(pjp);
    }

    public Object logInformation(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = pjp.getSignature().toShortString();
        String fullMethodName = pjp.getSignature().toLongString();
        Object[] args = pjp.getArgs();
        String threadName = Thread.currentThread().getName();

        log.info(">>> Calling method : {} (fullname: {} ) ", methodName, fullMethodName);
        log.info(">>> Thread: {}", threadName);
        log.info(">>> Arguments: {}", Arrays.toString(args));

        Object result = null;
            result = pjp.proceed();
            return result;
        } catch (Throwable ex) {
            log.error(">>> The method {} closed with exception: {}", methodName, ex.getMessage(), ex);
            throw ex;
        } finally {
            long executTime = System.currentTimeMillis() - startTime;
            log.info(">>> The method {}  worked in {} ms. Result {}", methodName, executTime, result);
        }
    }

    @Around("execution(* de.telran.gartenshop.controller..*(..)))")
    public Object controllerLogger(ProceedingJoinPoint pjp) throws Throwable {
        return logInformation(pjp);
    }
}