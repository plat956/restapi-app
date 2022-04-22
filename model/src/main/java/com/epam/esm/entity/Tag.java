package com.epam.esm.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tags")
@Data
@NoArgsConstructor
public class Tag extends BaseEntity<Long> {

    @Column(nullable = false, unique = true, length = 20)
    @Size(max = 20, message = "Tag name must be less than or equal to 20 chars")
    @NotBlank(message = "Please provide a tag name")
    private String name;
}
