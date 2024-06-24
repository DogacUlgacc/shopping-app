package com.enoca.enocaTask.controller;

import com.enoca.enocaTask.dto.CartItemDto;
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

    /*
    * Kullanıcının sahip olduğu Id ile cartın Id değerleri aynı oluyor Her kullanıcı oluşturulduğunda DB' de o kullanıcı
    * için bir adet de cart oluşturuluyor. Bu method ile kullanıcı kendi cart'ında bulanan productları ,fiyatları ve adetleri
    * görebiliyor..
    */

    @GetMapping("/{cartId}/items")
    public ResponseEntity<List<CartItemDto>> getCart(@PathVariable Long cartId) {
        List<CartItemDto> itemsInCartDto = cartService.getCartItems(cartId);
        if (itemsInCartDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(itemsInCartDto);
    }

    /*
    * Bu kısmı Customer silinirken kullanıyoruz. EmptyCart() cartItem classında!
    */
    @DeleteMapping("{cartId}")
    public void deleteCart(@PathVariable Long cartId){
        cartService.deleteCart(cartId);
    }
}
