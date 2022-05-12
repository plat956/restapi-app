package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.hateoas.server.core.Relation;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@Relation(collectionRelation = "orders")
public class Order extends BaseEntity<Long>{

    @ManyToMany
    @JoinTable(name = "order_certificate",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "gift_certificate_id")
    )
    @ToString.Exclude
    private List<GiftCertificate> giftCertificates = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @Column(nullable = false)
    private BigDecimal cost;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime purchaseDate;
}
