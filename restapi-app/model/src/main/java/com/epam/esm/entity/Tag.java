package com.epam.esm.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tags")
@Data
@NoArgsConstructor
@Relation(collectionRelation = "tags")
public class Tag extends BaseEntity<Long> {

    @Column(nullable = false, unique = true, length = 20)
    @Size(max = 20, message = "{validation.restrictions.tag.name}")
    @NotBlank(message = "{validation.error.tag.name}")
    private String name;
}
