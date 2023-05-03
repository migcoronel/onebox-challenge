package com.example.challenge.configuration.handler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    public static String PREFIX = "";
    private static final Logger LOG = LoggerFactory.getLogger(CustomizedResponseEntityExceptionHandler.class);

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalStateException(IllegalStateException ex, WebRequest request) {
        String errorMessage = String.format("Illegal State Exception: %s", ex.getMessage());
        LOG.error(errorMessage);
        List<String> errors = Collections.singletonList(ex.getMessage());
        return details(ApiExceptionType.NOT_FOUND, errorMessage, request.getDescription(false), errors);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public final ResponseEntity<Object> handleUOE(UnsupportedOperationException ex, WebRequest request) {
        String errorMessage = String.format("Unsupported Operation exception: %s", ex.getMessage());
        LOG.error(errorMessage);
        List<String> errors = Collections.singletonList(ex.getMessage());
        return details(ApiExceptionType.NOT_IMPLEMENTED, errorMessage, request.getDescription(false), errors);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        String errorMessage = String.format("Unexpected exception: %s", ex.getMessage());
        LOG.error(errorMessage);
        List<String> errors = Collections.singletonList(ex.getMessage());
        return details(ApiExceptionType.SYSTEM_ERROR, errorMessage, request.getDescription(false), errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();
        List<String> errors = Collections.singletonList(error);
        return details(ApiExceptionType.PARAM_TYPE_MISMATCH, error, request.getDescription(false), errors);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
        MissingServletRequestParameterException ex, HttpHeaders headers,
        HttpStatus status, WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";
        List<String> errors = Collections.singletonList(error);

        return details(ApiExceptionType.MISSING_PARAM, error, request.getDescription(false), errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(
        ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<String>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getPropertyPath() + ": " + violation.getMessage());
        }
        String errorMessage = String.format("Exception in request validation with cause = \'%s\' and exception = \'%s\'",
                ex.getCause(),
                ex.toString());
        LOG.error(errorMessage);
        return details(ApiExceptionType.INVALID_JSON, errorMessage, request.getDescription(false), errors);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        String errorMessage = String.format("Exception method argument not valid with cause = \'%s\' and exception = \'%s\'",
                ex.getCause(),
                ex.toString());
        LOG.error(errorMessage);
        return details(ApiExceptionType.INVALID_DATA, errorMessage, request.getDescription(false), errors);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<String>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.add(fieldError.getField() + " " + fieldError.getRejectedValue() + ": " + fieldError.getDefaultMessage());
        }
        return details(ApiExceptionType.INVALID_DATA, "Invalid Data Exception", request.getDescription(false), errors);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        String errorMessage = String.format("Exception in request with cause = \'%s\' and exception = \'%s\'",
                ex.getCause(),
                ex.toString());
        LOG.error(errorMessage);
        List<String> errors = Collections.singletonList(ex.getMessage());
        return details(ApiExceptionType.MALFORMED_JSON, errorMessage, request.getDescription(false), errors);
    }

    private ResponseEntity<Object> details(ApiExceptionType type, String message, String details, Collection<String> errors) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .code(PREFIX + type.getCode())
                .subcode(type.getSubcode())
                .message(message)
                .details(details)
                .errors(errors)
                .build();

        return new ResponseEntity<>(errorDetails, type.getStatus());
    }
}

