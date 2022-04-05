package com.epam.esm.controller;

import com.epam.esm.exception.ResourceError;
import com.epam.esm.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResourceError notFound(ResourceNotFoundException e) {
        return new ResourceError(40401, String.format("Requested resource not found (id = %d)", e.getResourceId()));
    }
}
