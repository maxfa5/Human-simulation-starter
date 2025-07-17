package org.project.validator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.format.DateTimeParseException;
import java.time.OffsetDateTime;
import org.project.anotation.Iso8601Time;

public class Iso8601TimeValidator implements ConstraintValidator<Iso8601Time, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) return true;
        try {
            OffsetDateTime.parse(value);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
