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


@RestController
@RequestMapping("/cart-items")
public class CartItemController {

    private final CartItemService cartItemService;


    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }
    /*
     * Bu method ile cartItem içerisine yeni ürünler ekleniyor. Eklenen ürünün stok miktarı product içerisindeki stokta azaltılıyor.
     * Eğer yeterli stok yoksa hata mesajı dönüyor ve cartItem içerisine o ürün eklenemiyor.
     * Eğer o product daha önce cartta varsa ürünün miktarını artırıyoruz. Daha önce o product eklenmemişse yeni cartItem oluşuyor
     *
     *  * */
    @PostMapping("{userId}/add")
    public ResponseEntity<?> addProductToCart(@RequestBody CartItemDto cartItemDto, @PathVariable Long userId) {
        return cartItemService.addProductToCart(cartItemDto, userId);
    }

    /*
     UpdateCartItem() methodu ile Customer'ın belli Product'ının cartItem içersindeki quantitysini değiştirebiliyoruz.
     Eğer cartItem içindeki miktarı artırırsak Product içerisindeki stok azalıyor. Miktarı azaltırsak da stok artırılıyor.
     Aynı zamanda bu metodun service kısmında cart tablosu içerisindeki total_price kısmı da ürünün fiyat değişimine göre
     kendisini update ediyor ve yeni toplam fiyatı gösteriyor.
    */
    @PutMapping("/update/{cartId}/{productId}")
    public ResponseEntity<?> updateCart(
            @PathVariable("cartId") Long cartId,
            @PathVariable("productId") Long productId,
            @RequestBody CartItemUpdateDto cartItemUpdateDto) {
        cartItemService.updateCartItem(cartId, productId, cartItemUpdateDto);
        return ResponseEntity.ok("Cart item güncellendi");
    }

    /*
     * Customer'ın hangi Product'ını silmek istiyorsak silebiliyoruz.
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
