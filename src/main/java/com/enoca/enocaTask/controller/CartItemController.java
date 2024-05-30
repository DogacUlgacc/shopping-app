package com.enoca.enocaTask.controller;

import com.enoca.enocaTask.entity.CartItem;
import com.enoca.enocaTask.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cart-items")
public class CartItemController {

private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getAllCartItems() {
        List<CartItem> cartItems = cartItemService.getAllCartItems();
        return ResponseEntity.ok(cartItems);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartItem> getCartItemById(@PathVariable Long id) {
        Optional<CartItem> cartItem = cartItemService.getCartItemById(id);
        return cartItem.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CartItem> createCartItem(@RequestBody CartItem cartItem) {
        CartItem savedCartItem = cartItemService.saveCartItem(cartItem);
        return ResponseEntity.ok(savedCartItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartItem> updateCartItem(@PathVariable Long id, @RequestBody CartItem cartItemDetails) {
        Optional<CartItem> cartItem = cartItemService.getCartItemById(id);
        if (cartItem.isPresent()) {
            CartItem existingCartItem = cartItem.get();
            existingCartItem.setQuantity(cartItemDetails.getQuantity());
            existingCartItem.setPrice(cartItemDetails.getPrice());
            CartItem updatedCartItem = cartItemService.saveCartItem(existingCartItem);
            return ResponseEntity.ok(updatedCartItem);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long id) {
        cartItemService.deleteCartItem(id);
        return ResponseEntity.ok().build();
    }
}
