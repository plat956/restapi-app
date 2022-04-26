package com.epam.esm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Relation(collectionRelation = "user_statistics")
public class UserStatisticsDto {

    private Long userId;
    private BigDecimal maxAmount;
    private Set<TagStatisticsDto> tags = new HashSet<>();

    public UserStatisticsDto(Long userId, BigDecimal maxAmount) {
        this.userId = userId;
        this.maxAmount = maxAmount;
    }
}
