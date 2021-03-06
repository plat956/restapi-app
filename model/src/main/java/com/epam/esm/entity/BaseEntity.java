package com.epam.esm.entity;

import com.epam.esm.listener.AuditListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
@EntityListeners(AuditListener.class)
@Data
@NoArgsConstructor
@JsonIgnoreProperties("hibernateLazyInitializer")
public abstract class BaseEntity<T> extends RepresentationModel implements Serializable {

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
