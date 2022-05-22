package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Relation(collectionRelation = "user_statistics")
public class UserStatisticsDto extends RepresentationModel<UserStatisticsDto> {

    private Long userId;
    private BigDecimal maxAmount;
    private Long tagId;
    private Integer tagCount;
}
