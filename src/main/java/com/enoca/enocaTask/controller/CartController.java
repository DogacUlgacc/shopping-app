package com.enoca.enocaTask.controller;

import com.enoca.enocaTask.dto.CartItemDto;
import com.enoca.enocaTask.entity.Cart;
import com.enoca.enocaTask.entity.CartItem;
import com.enoca.enocaTask.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{cartId}/items")
    public ResponseEntity<List<CartItemDto>> GetCart(@PathVariable Long cartId) {
        Cart cart = cartService.getCartById(cartId);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }
        List<CartItemDto> itemsInCartDto = new ArrayList<>();
        for (CartItem cartItem : cart.getItems()) {
            CartItemDto cartItemDto = new CartItemDto(
                    cartItem.getId(),
                    cartItem.getProduct().getId(),
                    cartItem.getQuantity(),
                    cartItem.getPrice()
            );
            itemsInCartDto.add(cartItemDto);
        }

        return ResponseEntity.ok(itemsInCartDto);
    }

    // Sepetteki ürünün miktarını güncelleme
    @PostMapping("/{cartId}/update")
    public ResponseEntity<Cart> UpdateCart(@PathVariable Long cartId, @RequestParam Long productId, @RequestParam int quantity) {
        cartService.updateProductQuantityInCart(cartId, productId, quantity);
        Cart updatedCart = cartService.getCartById(cartId);
        return ResponseEntity.ok(updatedCart);
    }

    @PostMapping("/{cartId}/add")
    public ResponseEntity<Cart> addProductToCart(@PathVariable Long cartId, @RequestParam Long productId, @RequestParam int quantity) {
        cartService.addProductToCart(cartId, productId, quantity);
        Cart updatedCart = cartService.getCartById(cartId);
        return ResponseEntity.ok(updatedCart);
    }

    // Sepetten ürün çıkarma
    @PostMapping("/{cartId}/remove")
    public ResponseEntity<Cart> removeProductFromCart(@PathVariable Long cartId, @RequestParam Long productId) {
        cartService.removeProductFromCart(cartId, productId);
        Cart updatedCart = cartService.getCartById(cartId);
        return ResponseEntity.ok(updatedCart);
    }
}
