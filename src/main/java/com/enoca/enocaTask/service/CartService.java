package com.enoca.enocaTask.service;

import com.enoca.enocaTask.entity.Cart;
import com.enoca.enocaTask.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
       }

    public List<Cart> getAllShoppingCart() {
        return cartRepository.findAll();
    }

    public Cart getCartById(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    public Cart addCart(Cart cart) {
        return cartRepository.save(cart);
    }
    public void deleteCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }
}
