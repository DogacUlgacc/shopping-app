package com.enoca.enocaTask.controller;

import com.enoca.enocaTask.dto.CartItemDto;
import com.enoca.enocaTask.dto.CartItemUpdateDto;
import com.enoca.enocaTask.entity.Cart;
import com.enoca.enocaTask.entity.CartItem;
import com.enoca.enocaTask.entity.Product;
import com.enoca.enocaTask.service.CartItemService;
import com.enoca.enocaTask.service.CartService;
import com.enoca.enocaTask.service.ProductService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> addProductToCart(@RequestBody CartItemDto cartItemDto) {

        Product product = productService.getProductById(cartItemDto.getProductId());
        long stockQuantity = product.getStockQuantity();
        Cart cart = cartService.getCartById(cartItemDto.getCartId());
        double price = cartItemDto.getQuantity() * product.getPrice();

        CartItem existingCartItem = cartItemService.getCartItemByProductAndCart(product, cart);

        if (existingCartItem != null && (stockQuantity >= cartItemDto.getQuantity())) {
            // Eğer daha önce varsa her şeyi update et. Productın db'deki stock miktarını da update ediyoruz.
            product.setStockQuantity(product.getStockQuantity() - cartItemDto.getQuantity());
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemDto.getQuantity());
            existingCartItem.setPrice(existingCartItem.getPrice() + price);
            CartItem savedCartItem = cartItemService.saveCartItem(existingCartItem);

            cart.setTotalPrice(cart.getTotalPrice() + price);
            cartService.addCart(cart);
            return ResponseEntity.ok(savedCartItem);

        } else if (stockQuantity >= cartItemDto.getQuantity()) {
            // Yoksa yeni oluştur
            CartItem cartItem = new CartItem();
            //Product db'de stock azalt!
            product.setStockQuantity(product.getStockQuantity() - cartItemDto.getQuantity());

            cartItem.setProduct(product);
            cartItem.setQuantity(cartItemDto.getQuantity());
            cartItem.setPrice(price);
            cartItem.setCart(cart);

            CartItem savedCartItem = cartItemService.saveCartItem(cartItem);
            cart.setTotalPrice(cart.getTotalPrice() + price);

            cartService.addCart(cart);
            return ResponseEntity.ok(savedCartItem);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Stok yetersiz");
        }
    }



    /*
   UpdateCartItem() methodu ile hangi customerın hangi productının quantitysini istiyorsak değiştirebiliyoruz.
   * Aynı zamanda bu metodun service kısmında cart tablosu içerisindeki total_price kısmı da ürünün fiyat değişimine göre
   * kendisini update ediyor ve yeni toplam fiyatı gösteriyor.
   * */
    @PutMapping("/update/{cartId}/{productId}")
    public ResponseEntity<?> updateCartItem(
            @PathVariable("cartId") Long cartId,
            @PathVariable("productId") Long productId,
            @RequestBody CartItemUpdateDto cartItemUpdateDto)
    {
        cartItemService.updateCartItem(cartId, productId, cartItemUpdateDto);
        return ResponseEntity.ok("Cart item güncellendi");
    }

    /*
     *RemoveProductFromCart() metodu ile hangi Customerın hangi productını silmek istiyorsak silebiliyoruz.
     * Daha sonra cart içerisinde total_price tekrar update ediliyor.
     * Product içerisindeki stok miktarı da silinen cartItemdaki quantity miktarına göre update ediliyor.
     * */
    @DeleteMapping("/delete/{cartItemId}/{productId}")
    public ResponseEntity<Void> RemoveProductFromCart(@PathVariable Long cartItemId, @PathVariable Long productId) {

        cartItemService.deleteCartItem(cartItemId, productId);
        return ResponseEntity.ok().build();
    }

    /*
     * EmptyCart() methodu ile bir customer kendi cart'ı içerisindeki bütün productları silebiliyor.
     * Bu endpoint ile aynı zamanda cart tablosu içerisinde bulunan cart'ın total_price'ı service kısmında 0 olarak set ediliyor.
     * */
    @DeleteMapping("/empty/{cartId}")
    public ResponseEntity<String> emptyCart(@PathVariable Long cartId) {
        cartItemService.emptyCart(cartId);
        return ResponseEntity.ok("Cart boşaltıldı!");
    }

}
