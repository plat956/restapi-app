package com.epam.esm.exception;

public class ResourceError {

    private int errorCode;
    private Object errorMessage;

    public ResourceError(int errorCode, Object errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public Object getErrorMessage() {
        return errorMessage;
    }
}