package com.enoca.enocaTask.service;

import com.enoca.enocaTask.dto.ProductDto;
import com.enoca.enocaTask.entity.Product;
import com.enoca.enocaTask.repository.CartItemRepository;
import com.enoca.enocaTask.repository.CartRepository;
import com.enoca.enocaTask.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public ProductService(CartRepository cartRepository, ProductRepository productRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
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
        cartItemRepository.deleteByProductId(productId);
        productRepository.deleteById(productId);
      }
}
