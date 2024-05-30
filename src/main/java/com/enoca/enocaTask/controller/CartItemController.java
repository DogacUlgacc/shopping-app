package com.enoca.enocaTask.controller;

import com.enoca.enocaTask.dto.CartItemDto;
import com.enoca.enocaTask.entity.Cart;
import com.enoca.enocaTask.entity.CartItem;
import com.enoca.enocaTask.entity.Product;
import com.enoca.enocaTask.service.CartItemService;
import com.enoca.enocaTask.service.CartService;
import com.enoca.enocaTask.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

/*    @GetMapping
    public ResponseEntity<List<CartItem>> getAllCartItems() {
        List<CartItem> cartItems = cartItemService.getAllCartItems();
        return ResponseEntity.ok(cartItems);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartItem> getCartItemById(@PathVariable Long id) {
        Optional<CartItem> cartItem = cartItemService.getCartItemById(id);
        return cartItem.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }*/

    @PostMapping("/add")
    public ResponseEntity<CartItem> AddProductToCart(@RequestBody CartItemDto cartItemDto) {
        CartItem cartItem = new CartItem();
        Product product =  productService.getProductById(cartItemDto.getProductId());
        Cart cart = cartService.getCartById(cartItemDto.getCartId());
        double price = cartItemDto.getQuantity() * product.getPrice();


        cartItem.setProduct(product);
        cartItem.setQuantity(cartItemDto.getQuantity());
        cartItem.setPrice(price);
        cartItem.setCart(cart);
        CartItem savedCartItem = cartItemService.saveCartItem(cartItem);

        cart.setTotalPrice(cart.getTotalPrice() + price);
        cartService.addCart(cart);

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

/*CartItem toSave = new CartItem();
        toSave.setProduct(cartItem.getProduct());
        toSave.setCart(cartItem.getCart());
        toSave.setQuantity(cartItem.getQuantity());
        double price = cartItem.getPrice() * cartItem.getQuantity();
        toSave.setPrice(price);
        */