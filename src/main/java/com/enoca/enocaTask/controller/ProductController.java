package com.enoca.enocaTask.controller;

import com.enoca.enocaTask.dto.ProductDto;
import com.enoca.enocaTask.entity.Product;
import com.enoca.enocaTask.service.ProductService;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@Data
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    public List<Product> getAllProduct() {
        return productService.getAllProducts();
    }

    @GetMapping("/{productId}")
    public Product getProductById(@PathVariable Long productId) {
        return productService.getProductById(productId);
    }

    /*
     * Yeni Product oluşturur ve DB'ye kaydeder.
     * */
    @PostMapping("/add")
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    /*
     * Product'ı update eder.
     */

    @PutMapping("update/{productId}")
    public Product UpdateProduct(@RequestBody ProductDto newProduct, @PathVariable Long productId) {
        return productService.updateProduct(newProduct, productId); 
    }


    @DeleteMapping("/delete/{productId}")
    public void DeleteProduct(@PathVariable Long productId) {
        productService.deleteProductById(productId);

    }
}
