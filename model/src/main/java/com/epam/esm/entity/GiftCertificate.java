package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class GiftCertificate extends BaseEntity<Long> {

    @Size(max = 80, message = "Name must be less than or equal to 80 chars")
    @NotBlank(message = "Please provide the name")
    private String name;

    @Size(max = 255, message = "Description must be less than or equal to 255 chars")
    @NotBlank(message = "Please provide the description")
    private String description;

    @Positive(message = "Price must be positive")
    @DecimalMax(value = "999999.99", message = "Price must be less than a million")
    @NotNull(message = "Please provide the price")
    private BigDecimal price;

    @Positive(message = "Duration must be positive")
    @Max(value = 365, message = "Duration must be less than a year")
    @NotNull(message = "Please provide the duration")
    private Integer duration;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastUpdateDate;

    private Set<@Valid Tag> tags = new HashSet<>();
}
