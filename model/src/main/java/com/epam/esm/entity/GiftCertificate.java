package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.hateoas.server.core.Relation;

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
@Relation(collectionRelation = "certificates")
public class GiftCertificate extends BaseEntity<Long> {

    @Column(nullable = false, length = 80)
    @Size(max = 80, message = "{validation.restrictions.certificate.name}")
    @NotBlank(message = "{validation.error.certificate.name}")
    private String name;

    @Column
    @Size(max = 255, message = "{validation.restrictions.certificate.descr}")
    private String description;

    @Column(nullable = false)
    @Positive(message = "{validation.restrictions.certificate.price-left}")
    @DecimalMax(value = "999999.99", message = "{validation.restrictions.certificate.price-right}")
    @NotNull(message = "{validation.error.certificate.price}")
    private BigDecimal price;

    @Column(nullable = false)
    @Positive(message = "{validation.restrictions.certificate.duration-left}")
    @Max(value = 365, message = "{validation.restrictions.certificate.duration-right}")
    @NotNull(message = "{validation.error.certificate.duration}")
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
