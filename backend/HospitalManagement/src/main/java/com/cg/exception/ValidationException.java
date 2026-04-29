package com.cg.exception;

import java.util.List;
import org.springframework.validation.FieldError;

public class ValidationException extends RuntimeException {

    private List<FieldError> errors;

    public ValidationException(List<FieldError> errors) {
        this.errors = errors;
    }

    public List<FieldError> getErrors() {
        return errors;
    }
}