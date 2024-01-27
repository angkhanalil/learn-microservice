package com.microservice.orderservice.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemsDto {
    private Long id;
    private String sku;
    private BigDecimal price;
    private Integer qty;
}
