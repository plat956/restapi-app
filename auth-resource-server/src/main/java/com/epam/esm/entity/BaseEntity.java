package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
@Data
@NoArgsConstructor
@JsonIgnoreProperties("hibernateLazyInitializer")
public abstract class BaseEntity<T> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private T id;

    @JsonIgnore
    @Column(nullable = false)
    private String operation;

    @JsonIgnore
    @Column(nullable = false)
    private Long timestamp;
}
