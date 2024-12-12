package com.enoca.enocaTask.service;

import com.enoca.enocaTask.dto.ProductDto;
import com.enoca.enocaTask.entity.Cart;
import com.enoca.enocaTask.entity.CartItem;
import com.enoca.enocaTask.entity.Product;
import com.enoca.enocaTask.repository.CartItemRepository;
import com.enoca.enocaTask.repository.CartRepository;
import com.enoca.enocaTask.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;

    public ProductService(ProductRepository productRepository, CartItemRepository cartItemRepository, CartRepository cartRepository) {

        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }

    public Product addProduct(Product product) {
        if (product.getQuantity() <= 0 && product.getPrice() <= 0) {
            return null;
        } else {
            return productRepository.save(product);
        }
    }

    public Product updateProduct(ProductDto newProduct, Long productId) {
        Product updateProduct = productRepository.getReferenceById(productId);
        updateProduct.setName(newProduct.getName());
        updateProduct.setPrice(newProduct.getPrice());
        updateProduct.setQuantity(newProduct.getQuantity());
        return productRepository.save(updateProduct);
    }

    @Transactional
    public void deleteProductById(Long productId) {
        List<Long> cartIds = cartItemRepository.getCartIdsByProductId(productId);

        List<Long> cartItemIds = cartItemRepository.getCartItemIdsByProductId(productId);


        for (Long cartId : cartIds) {
            Cart cartForUpdating = cartRepository.getReferenceById(cartId);
            double totalPriceToSubtract = 0;
            for (Long cartItemId : cartItemIds) {
                CartItem cartItem = cartItemRepository.getReferenceById(cartItemId);
                if (cartItem.getCart().getId().equals(cartId)) {
                    totalPriceToSubtract += cartItem.getProduct().getPrice() * cartItem.getQuantity();
                }
            }

            cartForUpdating.setTotalPrice(cartForUpdating.getTotalPrice() - totalPriceToSubtract);
        }
        cartItemRepository.deleteByProductId(productId);
        productRepository.deleteById(productId);

    }
}
