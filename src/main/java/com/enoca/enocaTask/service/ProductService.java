package com.enoca.enocaTask.service;

import com.enoca.enocaTask.dto.ProductDto;
import com.enoca.enocaTask.entity.CartItem;
import com.enoca.enocaTask.entity.Product;
import com.enoca.enocaTask.repository.CartItemRepository;
import com.enoca.enocaTask.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    public ProductService(ProductRepository productRepository, CartItemRepository cartItemRepository, CartItemRepository cartItemRepository1) {
        this.productRepository = productRepository;

        this.cartItemRepository = cartItemRepository1;
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


    public void deleteProductById(Long productId) {
        cartItemRepository.deleteByProductId(productId);
        productRepository.deleteById(productId);
    }
}
