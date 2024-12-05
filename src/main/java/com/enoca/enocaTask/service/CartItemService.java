package com.enoca.enocaTask.service;

import com.enoca.enocaTask.dto.CartItemDto;
import com.enoca.enocaTask.dto.CartItemUpdateDto;
import com.enoca.enocaTask.entity.Cart;
import com.enoca.enocaTask.entity.CartItem;
import com.enoca.enocaTask.entity.Customer;
import com.enoca.enocaTask.entity.Product;
import com.enoca.enocaTask.exception.CartItemNotFoundEx;
import com.enoca.enocaTask.exception.StockException;
import com.enoca.enocaTask.repository.CartItemRepository;
import com.enoca.enocaTask.repository.CartRepository;

import com.enoca.enocaTask.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductService productService;
    private final CartService cartService;
    private final CustomerRepository customerRepository;

    public CartItemService(CartItemRepository cartItemRepository, CartRepository cartRepository, ProductService productService, CartService cartService, CustomerRepository customerRepository) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.cartService = cartService;
        this.customerRepository = customerRepository;
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
        long productStock = product.getQuantity();
        long difference = cartItemUpdateDto.getQuantity() - optionalCartItem.get().getQuantity();
        if (optionalCartItem.isPresent()) {
            CartItem cartItem = optionalCartItem.get();

            //Update edilirken quantity artırılıyorsa productın stoğu azalt!
            if (cartItem.getQuantity() <= cartItemUpdateDto.getQuantity()) {
                if (productStock >= cartItemUpdateDto.getQuantity()) {
                    product.setQuantity((int) productStock + (int) difference);
                    // Eski ürün fiyatını al
                    calculatePriceAndUpdateDb(cartItem, cartItemUpdateDto);
                   /* double oldItemPrice = cartItem.getPrice();

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
                    cartRepository.save(cart);*/
                } else {
                    throw new StockException("Stok yetersiz");
                }
            }

            //Update edilirken quantity artırılıyorsa productın stoğu artır!
            if (cartItem.getQuantity() >= cartItemUpdateDto.getQuantity()) {
                product.setQuantity((int) productStock - (int) difference);
                // Eski ürün fiyatını al
                calculatePriceAndUpdateDb(cartItem, cartItemUpdateDto);

            }
        } else {
            throw new StockException("Cart item bulunamadı cartId: " + cartId + " productId: " + productId);
        }
    }

    @Transactional
    public void deleteCartItem(Long cartId, Long productId) {
        Product product = productService.getProductById(productId);
        try {
            Optional<CartItem> optionalCartItem = cartItemRepository.findByCartIdAndProductId(cartId, productId);
            // Optional içindeki CartItem'ı al, eğer yoksa hata fırlat
            CartItem cartItem = optionalCartItem.orElseThrow(() -> new CartItemNotFoundEx("CartItem bulunamadı."));

            //Stoğu product üzerinde güncelle
            double quantity = cartItem.getQuantity();
            product.setQuantity(product.getQuantity() + (int) quantity);

            // Toplam fiyatı güncelle
            Cart cart = cartItem.getCart();
            double subtractPrice = cartItem.getPrice();
            cart.setTotalPrice(cart.getTotalPrice() - subtractPrice);

            cartItemRepository.delete(cartItem);
        } catch (Exception e) {
            throw new CartItemNotFoundEx("CartItem silinirken hata oluştu.");
        }
    }

    public void emptyCart(Long cartId) {
        Cart cart = cartRepository.getReferenceById(cartId);
        cart.setTotalPrice(0);
        cartItemRepository.deleteByCartId(cartId);
    }

    public ResponseEntity<?> addProductToCart(CartItemDto cartItemDto, Long userId) {
        Product product = productService.getProductById(cartItemDto.getProductId());
        long stockQuantity = product.getQuantity();

        Cart cart = cartService.getCartById(userId);
        double price = cartItemDto.getQuantity() * product.getPrice();

        CartItem existingCartItem = this.getCartItemByProductAndCart(product, cart);

        if (existingCartItem != null && (stockQuantity >= cartItemDto.getQuantity())) {
            // Eğer daha önce varsa her şeyi update et. Productın db'deki stock miktarını da update ediyoruz.
            product.setQuantity(product.getQuantity() - cartItemDto.getQuantity());
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemDto.getQuantity());
            existingCartItem.setPrice(existingCartItem.getPrice() + price);
            CartItem savedCartItem = this.saveCartItem(existingCartItem);

            cart.setTotalPrice(cart.getTotalPrice() + price);
            cartService.addCart(cart);
            return ResponseEntity.ok(savedCartItem);

        } else if (stockQuantity >= cartItemDto.getQuantity()) {
            // Yoksa yeni oluştur
            CartItem cartItem = new CartItem();
            //Product db'de stock azalt!
            product.setQuantity(product.getQuantity() - cartItemDto.getQuantity());

            cartItem.setProduct(product);
            cartItem.setQuantity(cartItemDto.getQuantity());
            cartItem.setPrice(price);
            cartItem.setCart(cart);

            CartItem savedCartItem = this.saveCartItem(cartItem);
            cart.setTotalPrice(cart.getTotalPrice() + price);

            cartService.addCart(cart);
            return ResponseEntity.ok(savedCartItem);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Stok yetersiz");
        }
    }

    public void calculatePriceAndUpdateDb(CartItem cartItem, CartItemUpdateDto cartItemUpdateDto) {
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
}
