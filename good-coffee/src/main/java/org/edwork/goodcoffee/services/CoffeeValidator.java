package org.edwork.goodcoffee.services;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;

import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;

public class CoffeeValidator {

    private final Validator validator;

    @Inject
    public CoffeeValidator(Validator validator) {
        this.validator = validator;
    }

    public void validate(Object object) {
        Set<ConstraintViolation<Object>> violations = validator.validate(object);

        Optional<String> violationMessages =
                violations.stream().map(Object::toString).reduce((a, b) -> String.join(",", a, b));

        violationMessages.ifPresent(message -> { throw new ValidationException(message); });

    }
}
