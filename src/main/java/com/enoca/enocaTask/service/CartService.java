package com.enoca.enocaTask.service;

import com.enoca.enocaTask.entity.Cart;
import com.enoca.enocaTask.entity.CartItem;
import com.enoca.enocaTask.entity.Product;
import com.enoca.enocaTask.repository.CartItemRepository;
import com.enoca.enocaTask.repository.CartRepository;
import com.enoca.enocaTask.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }


    public List<Cart> getAllShoppingCart() {
        return cartRepository.findAll();
    }

    public Cart getShoppingCartById(Long cartId) {
        return cartRepository.findById(cartId).orElse(null);
    }


    public void deleteShoppingCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    public void addProductToCart(Long cartId, Long productId, int quantity) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            // Eğer cart bulunamazsa hata fırlat veya başka bir işlem yap
            throw new RuntimeException("Cart not found with id: " + cartId);
        }

        // Product'ı verilen ID ile al
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            // Eğer product bulunamazsa hata fırlat veya başka bir işlem yap
            throw new RuntimeException("Product not found with id: " + productId);
        }

        // CartItem oluştur
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setPrice(product.getPrice() * quantity); // Ürün fiyatı ve miktarıyla çarpılıp toplam fiyat hesaplanır

        // CartItem'ı kaydet
        cartItemRepository.save(cartItem);
    }

    public Cart getCartById(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    public void removeProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found in cart"));

        cart.getItems().remove(cartItem);
        cartItemRepository.delete(cartItem);

        // Update total price
        double totalPrice = cart.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        cart.setTotalPrice(totalPrice);

        cartRepository.save(cart);
    }

    public void updateProductQuantityInCart(Long cartId, Long productId, int quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found in cart"));

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        // Update total price
        double totalPrice = cart.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        cart.setTotalPrice(totalPrice);

        cartRepository.save(cart);
    }

    public Cart addCart(Cart cart) {
        return cartRepository.save(cart);
    }
}
