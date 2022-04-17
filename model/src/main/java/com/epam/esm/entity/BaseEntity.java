package com.epam.esm.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class BaseEntity<T> implements Serializable {

    private T id;
}
