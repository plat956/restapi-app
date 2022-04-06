package com.epam.esm.entity;

import java.io.Serializable;

public abstract class BaseEntity<T> implements Serializable {

    private T id;

    public BaseEntity() {
    }

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }
}
