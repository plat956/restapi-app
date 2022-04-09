package com.epam.esm.controller;

import com.epam.esm.exception.ResourceDuplicateException;
import com.epam.esm.exception.ResourceError;
import com.epam.esm.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResourceError resourceNotFound(ResourceNotFoundException e) {
        return new ResourceError(40401, String.format("Requested resource is not found (id = %d)", e.getResourceId()));
    }

    @ExceptionHandler(ResourceDuplicateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResourceError resourceDuplicate(ResourceDuplicateException e) {
        return new ResourceError(40001, String.format("The resource with %s '%s' already exists",
                e.getUniqueField(), e.getUniqueFieldValue()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResourceError invalidArguments(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .toList();
        return new ResourceError(40002, errors.size() == 1 ? errors.get(0) : errors);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResourceError internalError(Exception ex) {
        return new ResourceError(50001, "Something went wrong. We're working on solving the problem");
    }
}
