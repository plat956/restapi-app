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
    public ResourceError resourceNotFound(ResourceNotFoundException e) {
        return new ResourceError(40401, String.format("Requested resource is not found (id = %d)", e.getResourceId()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResourceError internalError() {
        return new ResourceError(500, "Something went wrong. We're working on solving the problem");
    }
}
