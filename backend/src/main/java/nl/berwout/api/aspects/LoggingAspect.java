package nl.berwout.api.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.aspectj.lang.ProceedingJoinPoint;


@Aspect
@Component
public class LoggingAspect {
    @Before("execution(* nl.berwout.api.controllers.*.*(..))")
    public void before(JoinPoint joinPoint){
        System.out.println(joinPoint.getSignature() + " gets Called");
    }

    @After("execution(* nl.berwout.api.controllers.*.*(..))")
    public void after(JoinPoint joinPoint){
        System.out.println(joinPoint.getSignature() + " is returned");
    }

    @Around("@annotation(nl.berwout.api.annotations.Timed) && execution(* *(..))")
    public Object aroundAdvice(ProceedingJoinPoint proceedingJoinPoint){
        Object returnObject = null;
        long startMillis = System.currentTimeMillis();
        try{
            System.out.println(proceedingJoinPoint.getSignature().getName() + " started at: " + startMillis);
            returnObject = proceedingJoinPoint.proceed();
        } catch (Throwable throwable){
            throwable.printStackTrace();
        } finally {
            long endMillis = System.currentTimeMillis();
            long duration = endMillis - startMillis;
            System.out.println(proceedingJoinPoint.getSignature().getName() + " ended at: " + endMillis + " total execution time: " + duration);
        }
        return returnObject;
    }
}
