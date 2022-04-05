package com.epam.esm.exception;

public class ResourceError {

    private int errorCode;
    private String errorMessage;

    public ResourceError(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}