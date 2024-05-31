package com.enoca.enocaTask.controller;

import com.enoca.enocaTask.dto.CartItemDto;
import com.enoca.enocaTask.dto.CartItemUpdateDto;
import com.enoca.enocaTask.entity.Cart;
import com.enoca.enocaTask.entity.CartItem;
import com.enoca.enocaTask.entity.Product;
import com.enoca.enocaTask.service.CartItemService;
import com.enoca.enocaTask.service.CartService;
import com.enoca.enocaTask.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/cart-items")
public class CartItemController {

private final CartItemService cartItemService;
private final ProductService productService;
private final CartService cartService;

    public CartItemController(CartItemService cartItemService, ProductService productService, CartService cartService) {
        this.cartItemService = cartItemService;
        this.productService = productService;
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<CartItem> AddProductToCart(@RequestBody CartItemDto cartItemDto) {
        Product product = productService.getProductById(cartItemDto.getProductId());
        Cart cart = cartService.getCartById(cartItemDto.getCartId());
        double price = cartItemDto.getQuantity() * product.getPrice();

        CartItem existingCartItem = cartItemService.getCartItemByProductAndCart(product, cart);

        if (existingCartItem != null) {
            // Eğer varsa update et
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemDto.getQuantity());
            existingCartItem.setPrice(existingCartItem.getPrice() + price);
            CartItem savedCartItem = cartItemService.saveCartItem(existingCartItem);

            cart.setTotalPrice(cart.getTotalPrice() + price);
            cartService.addCart(cart);
            return ResponseEntity.ok(savedCartItem);

        } else {
            // yoksa yeni oluştur
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(cartItemDto.getQuantity());
            cartItem.setPrice(price);
            cartItem.setCart(cart);
            CartItem savedCartItem = cartItemService.saveCartItem(cartItem);

            cart.setTotalPrice(cart.getTotalPrice() + price);
            cartService.addCart(cart);

            return ResponseEntity.ok(savedCartItem);
        }
    }

    @PutMapping("/update/{cartId}/{productId}")
    public ResponseEntity<?> updateCartItem(
            @PathVariable("cartId") Long cartId,
            @PathVariable("productId") Long productId,
            @RequestBody CartItemUpdateDto cartItemUpdateDto)
    {
        cartItemService.updateCartItem(cartId, productId, cartItemUpdateDto);
        return ResponseEntity.ok("Cart item updated successfully");
    }

    /*@PutMapping("/update/{cartId}")
    public ResponseEntity<CartItem> updateCartItem(@RequestBody CartItemUpdateDto cartItemUpdateDto,@PathVariable Long cartId) {

        Product product = productService.getProductById(cartItemUpdateDto.getProductId());
        Cart cart = cartService.getCartById(cartItemUpdateDto.getCartId());
        double newPrice = cartItemUpdateDto.getQuantity() * product.getPrice();

        CartItem existingCartItem = cartItemService.getCartItemByProductAndCart(product, cart);
        if (existingCartItem != null) {
            // Eğer varsa, miktarı güncelle
            double oldPrice = existingCartItem.getPrice();
            existingCartItem.setQuantity(cartItemUpdateDto.getQuantity());
            existingCartItem.setPrice(newPrice);
            CartItem savedCartItem = cartItemService.saveCartItem(existingCartItem);
            cart.setTotalPrice(cart.getTotalPrice() - oldPrice + newPrice);
            cartService.addCart(cart);

            return ResponseEntity.ok(savedCartItem);
        }else {
            // Eğer yoksa, yeni oluştur
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(cartItemUpdateDto.getQuantity());
            cartItem.setPrice(newPrice);
            cartItem.setCart(cart);
            CartItem savedCartItem = cartItemService.saveCartItem(cartItem);

            cart.setTotalPrice(cart.getTotalPrice() + newPrice);
            cartService.addCart(cart);

            return ResponseEntity.ok(savedCartItem);
        }

    }*/

    @DeleteMapping("/delete/{cartItemId}/{productId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long cartItemId, @PathVariable Long productId) {
        cartItemService.deleteCartItem(cartItemId, productId);
        return ResponseEntity.ok().build();
    }
}
