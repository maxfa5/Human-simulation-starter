package org.project.aspects;

import java.lang.reflect.Method;

import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.project.anotation.WeylandWatchingYou;
import org.project.model.AuditMode;
import org.project.service.AuditSenderKafkaService;
import org.springframework.beans.factory.annotation.Autowired;

@Aspect
public class AuditAspect {
    @Autowired
    private AuditSenderKafkaService auditSenderKafkaService;

    @Around("@annotation(org.project.anotation.WeylandWatchingYou)")
    public Object aroundCommandExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        WeylandWatchingYou annotation = method.getAnnotation(WeylandWatchingYou.class);

        AuditMode auditMode = annotation.auditMode();
        String auditTopic = annotation.auditTopic();

        String methodName = method.getName();
        Object[] args = joinPoint.getArgs();
        String params = java.util.Arrays.toString(args);

        String message = "Command execution started: " + methodName + ", params: " + params;
        Object result = joinPoint.proceed();
        
        message += ", finished with result: " + result;
        if (auditMode == AuditMode.CONSOLE) {
            System.out.println(message);
        }else{
            auditSenderKafkaService.sendAudit(message, auditMode, auditTopic);
        }

        return result;
    }
}
