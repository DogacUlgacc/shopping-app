package com.enoca.enocaTask.service;

import com.enoca.enocaTask.dto.CartItemUpdateDto;
import com.enoca.enocaTask.entity.Cart;
import com.enoca.enocaTask.entity.CartItem;
import com.enoca.enocaTask.entity.Product;
import com.enoca.enocaTask.repository.CartItemRepository;
import com.enoca.enocaTask.repository.CartRepository;
import com.enoca.enocaTask.repository.ProductRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductService productService;

    public CartItemService(CartItemRepository cartItemRepository, CartRepository cartRepository, ProductService productService) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.productService = productService;
    }

    public List<CartItem> getAllCartItems() {
        return cartItemRepository.findAll();
    }

    public Optional<CartItem> getCartItemById(Long id) {
        return cartItemRepository.findById(id);
    }

    public CartItem saveCartItem(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    public CartItem getCartItemByProductAndCart(Product product, Cart cart) {
        return cartItemRepository.findByProductAndCart(product, cart);
    }

    public void updateCartItem(Long cartId, Long productId, CartItemUpdateDto cartItemUpdateDto) {
        Optional<CartItem> optionalCartItem = cartItemRepository.findByCartIdAndProductId(cartId, productId);
        Product product = productService.getProductById(productId);
        long productStock = product.getStockQuantity();
        long difference = cartItemUpdateDto.getQuantity() - optionalCartItem.get().getQuantity(); // item 5 di 10 istedi  +5 çıkar diff
        if (optionalCartItem.isPresent()) {
            CartItem cartItem = optionalCartItem.get();

            //Update edilirken quantity artırılıyorsa productın stoğu azalt!
            if(cartItem.getQuantity()<= cartItemUpdateDto.getQuantity()){

                product.setStockQuantity((int)productStock + (int)difference);
                // Eski ürün fiyatını al
                double oldItemPrice = cartItem.getPrice();

                // Yeni adetle ürün fiyatını güncelle
                cartItem.setQuantity(cartItemUpdateDto.getQuantity());
                double newItemPrice = cartItemUpdateDto.getQuantity() * cartItem.getProduct().getPrice();
                cartItem.setPrice(newItemPrice);

                // Sepetin toplam fiyatını güncelle
                Cart cart = cartItem.getCart();
                double oldCartTotalPrice = cart.getTotalPrice();
                double newCartTotalPrice = oldCartTotalPrice - oldItemPrice + newItemPrice;
                cart.setTotalPrice(newCartTotalPrice);

                // DB güncelle
                cartItemRepository.save(cartItem);
                cartRepository.save(cart);
            }

            //Update edilirken quantity artırılıyorsa productın stoğu artır!
            if(cartItem.getQuantity()>= cartItemUpdateDto.getQuantity()){
                product.setStockQuantity((int)productStock - (int)difference);
                // Eski ürün fiyatını al
                double oldItemPrice = cartItem.getPrice();

                // Yeni adetle ürün fiyatını güncelle
                cartItem.setQuantity(cartItemUpdateDto.getQuantity());
                double newItemPrice = cartItemUpdateDto.getQuantity() * cartItem.getProduct().getPrice();
                cartItem.setPrice(newItemPrice);

                // Sepetin toplam fiyatını güncelle
                Cart cart = cartItem.getCart();
                double oldCartTotalPrice = cart.getTotalPrice();
                double newCartTotalPrice = oldCartTotalPrice - oldItemPrice + newItemPrice;
                cart.setTotalPrice(newCartTotalPrice);

                // DB güncelle
                cartItemRepository.save(cartItem);
                cartRepository.save(cart);
            }
        } else {
            throw new RuntimeException("Cart item bulunamadı cartId: " + cartId + " productId: " + productId);
        }
    }

    @Transactional
    public void deleteCartItem(Long cartId, Long productId) {
        Product product = productService.getProductById(productId);
        try {
            Optional<CartItem> optionalCartItem = cartItemRepository.findByCartIdAndProductId(cartId, productId);
            // Optional içindeki CartItem'ı al, eğer yoksa hata fırlat
            CartItem cartItem = optionalCartItem.orElseThrow(() -> new RuntimeException("CartItem bulunamadı."));

            //Stoğu product üzerinde güncelle
            double quantity = cartItem.getQuantity();
            product.setStockQuantity(product.getStockQuantity() + (int)quantity);

            // Toplam fiyatı güncelle
            Cart cart = cartItem.getCart();
            double subtractPrice = cartItem.getPrice();
            cart.setTotalPrice(cart.getTotalPrice() - subtractPrice);

            cartItemRepository.delete(cartItem);
        } catch (Exception e) {
            throw new RuntimeException("CartItem silinirken hata oluştu.", e);
        }
    }

    public void emptyCart(Long cartId) {
        Cart cart = cartRepository.getReferenceById(cartId);
        cart.setTotalPrice(0);
        cartItemRepository.deleteByCartId(cartId);
    }
}
