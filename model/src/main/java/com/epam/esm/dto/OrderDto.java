package com.epam.esm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderDto {

    private Long id;

    @NotNull(message = "Please provide gift certificate ids")
    private List<Long> certificates;

    private Long userId;

    private BigDecimal cost;

    private Long purchaseTimestamp;
}
