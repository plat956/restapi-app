package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Order extends BaseEntity<Long>{

    @NotNull(message = "Please provide the certificate")
    private GiftCertificate giftCertificate;

    @NotNull(message = "Please provide the user")
    private User user;

    @Positive(message = "Price must be positive")
    @DecimalMax(value = "999999.99", message = "Price must be less than a million")
    @NotNull(message = "Please provide the price")
    private BigDecimal price;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime purchaseDate;
}
