package com.epam.esm.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.hateoas.server.core.Relation;

import javax.persistence.*;
import java.math.BigDecimal;
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
    private Long purchaseTimestamp;
}
