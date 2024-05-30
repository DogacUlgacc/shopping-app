package com.enoca.enocaTask.dto;

public class CartItemUpdateDto {
    private int quantity;

    public CartItemUpdateDto(Long productId, Long cartId, int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
