package com.epam.esm.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class Tag extends BaseEntity<Long> {

    @Size(max = 20, message = "Tag name must be less than or equal to 20 chars")
    @NotBlank(message = "Please provide a tag name")
    private String name;
}
