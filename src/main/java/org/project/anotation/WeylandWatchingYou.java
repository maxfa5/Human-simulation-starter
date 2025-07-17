package org.project.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.project.model.AuditMode;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WeylandWatchingYou {
    AuditMode auditMode() default AuditMode.CONSOLE;
    String auditTopic() default "audit";
}
