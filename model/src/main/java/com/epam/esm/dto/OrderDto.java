package com.epam.esm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@Relation(collectionRelation = "orders")
public class OrderDto extends RepresentationModel<OrderDto> {

    private Long id;

    @NotEmpty(message = "{validation.error.order.ids}")
    private List<Long> certificates;

    private Long userId;

    private BigDecimal cost;

    private Long purchaseTimestamp;
}
