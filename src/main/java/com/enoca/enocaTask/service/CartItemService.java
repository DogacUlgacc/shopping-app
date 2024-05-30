package com.enoca.enocaTask.service;

import com.enoca.enocaTask.dto.CartItemUpdateDto;
import com.enoca.enocaTask.entity.Cart;
import com.enoca.enocaTask.entity.CartItem;
import com.enoca.enocaTask.entity.Product;
import com.enoca.enocaTask.repository.CartItemRepository;
import com.enoca.enocaTask.repository.CartRepository;
import com.enoca.enocaTask.repository.ProductRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemService {


    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public CartItemService(CartItemRepository cartItemRepository, ProductRepository productRepository, CartRepository cartRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
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

    public void deleteCartItem(Long id) {
        cartItemRepository.deleteById(id);
    }
    public CartItem getCartItemByProductAndCart(Product product, Cart cart) {
        return cartItemRepository.findByProductAndCart(product, cart);
    }

    public void updateCartItem(Long cartId, Long productId, CartItemUpdateDto cartItemUpdateDto) {
        Optional<CartItem> optionalCartItem = cartItemRepository.findByCartIdAndProductId(cartId, productId);

        if (optionalCartItem.isPresent()) {
            CartItem cartItem = optionalCartItem.get();

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

            // Veritabanında güncelle
            cartItemRepository.save(cartItem);
            cartRepository.save(cart);
        } else {
            throw new RuntimeException("Cart item bulunamadı cartId: " + cartId + " productId: " + productId);
        }
    }


}
