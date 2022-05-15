package com.epam.esm.exception;

public class ResourceDuplicateException extends RuntimeException{

    private String uniqueField;
    private Object uniqueFieldValue;

    public ResourceDuplicateException(String uniqueField, Object uniqueFieldValue) {
        this.uniqueField = uniqueField;
        this.uniqueFieldValue = uniqueFieldValue;
    }

    public String getUniqueField() {
        return uniqueField;
    }

    public Object getUniqueFieldValue() {
        return uniqueFieldValue;
    }
}
