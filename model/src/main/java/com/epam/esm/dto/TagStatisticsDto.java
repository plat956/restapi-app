package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
public class TagStatisticsDto extends RepresentationModel<TagStatisticsDto> {

    private Long tagId;
    private Integer count;
}
