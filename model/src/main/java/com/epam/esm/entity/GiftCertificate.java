package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "gift_certificates")
@Data
@NoArgsConstructor
public class GiftCertificate extends BaseEntity<Long> {

    @Column(nullable = false, length = 80)
    @Size(max = 80, message = "Name must be less than or equal to 80 chars")
    @NotBlank(message = "Please provide a name")
    private String name;

    @Column
    @Size(max = 255, message = "Description must be less than or equal to 255 chars")
    private String description;

    @Column(nullable = false)
    @Positive(message = "Price must be positive")
    @DecimalMax(value = "999999.99", message = "Price must be less than a million")
    @NotNull(message = "Please provide price")
    private BigDecimal price;

    @Column(nullable = false)
    @Positive(message = "Duration must be positive")
    @Max(value = 365, message = "Duration must be less than a year")
    @NotNull(message = "Please provide duration")
    private Integer duration;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createDate;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastUpdateDate;

    @ManyToMany
    @JoinTable(name = "gift_certificate_tag",
            joinColumns = @JoinColumn(name = "gift_certificate_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @ToString.Exclude
    private Set<@Valid Tag> tags = new HashSet<>();
}
