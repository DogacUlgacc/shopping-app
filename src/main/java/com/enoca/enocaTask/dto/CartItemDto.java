package com.enoca.enocaTask.dto;

public class CartItemDto
{
    private Long cartId;
    private Long productId;
    private int quantity;
    private double price;

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public CartItemDto(Long cartId, Long productId, int quantity, double price) {
        this.cartId = cartId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
