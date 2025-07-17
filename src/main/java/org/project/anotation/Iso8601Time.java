package org.project.anotation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.project.validator.Iso8601TimeValidator;

@Documented
@Constraint(validatedBy = Iso8601TimeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Iso8601Time {
    String message() default "Time must be in ISO 8601 format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
