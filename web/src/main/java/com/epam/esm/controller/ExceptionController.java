package com.epam.esm.controller;

import com.epam.esm.exception.ResourceDuplicateException;
import com.epam.esm.exception.ResourceError;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.util.MessageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;

@RestControllerAdvice
public class ExceptionController {

    private MessageProvider messageProvider;

    @Autowired
    public ExceptionController(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResourceError handleNotFoundError() {
        return new ResourceError(40401, messageProvider.getMessage("message.error.no-handlers"));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResourceError resourceNotFound(ResourceNotFoundException e) {
        String message = e.getResourceId() != null ?
                String.format(messageProvider.getMessage("message.error.no-resource"), e.getResourceId()) : e.getMessage();
        return new ResourceError(40402, message);
    }

    @ExceptionHandler(ResourceDuplicateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResourceError resourceDuplicate(ResourceDuplicateException e) {
        return new ResourceError(40001, String.format(messageProvider.getMessage("message.error.resource-duplicate"),
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

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResourceError invalidMethod(HttpRequestMethodNotSupportedException ex) {
        return new ResourceError(40003, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResourceError wrongArgument(MethodArgumentTypeMismatchException e) {
        return new ResourceError(40004, messageProvider.getMessage("message.error.wrong-param") + e.getName());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResourceError invalidBody() {
        return new ResourceError(40005, messageProvider.getMessage("message.error.wrong-body"));
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ResourceError badCredentialsExceptionException() {
        return new ResourceError(40301, messageProvider.getMessage("message.error.wrong-credentials"));
    }

    @ExceptionHandler(DisabledException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ResourceError disabledException() {
        return new ResourceError(40302, messageProvider.getMessage("message.error.account-disabled"));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResourceError internalError(Exception ex) {
        return new ResourceError(50001, messageProvider.getMessage("message.error.unexpected"));
    }
}
