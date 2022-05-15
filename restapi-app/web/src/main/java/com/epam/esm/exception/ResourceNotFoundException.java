package com.epam.esm.exception;

public class ResourceNotFoundException extends RuntimeException {

    private Long resourceId;

    public ResourceNotFoundException(Long resourceId) {
        this.resourceId = resourceId;
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public Long getResourceId() {
        return resourceId;
    }
}
