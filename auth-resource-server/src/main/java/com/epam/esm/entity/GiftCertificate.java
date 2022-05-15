package com.epam.esm.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "gift_certificates")
@Data
@NoArgsConstructor
public class GiftCertificate extends BaseEntity<Long> {

    @Column(nullable = false, length = 80)
    private String name;

    @Column
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false)
    private LocalDateTime createDate;

    @Column
    private LocalDateTime lastUpdateDate;
}
