package com.enoca.enocaTask.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CartItemDto {

    private Long productId;
    private String productName;
    private Double price;
    private int quantity;

    public CartItemDto(Long productId, int quantity, Double price, String productName) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.productName = productName;
    }
}