package com.enoca.enocaTask.controller;

import com.enoca.enocaTask.entity.Product;
import com.enoca.enocaTask.service.ProductService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@Data
public class ProductController  {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    public List<Product> getAllProduct(){
        return productService.getAllProducts();
    }

    @GetMapping("/{productId}")
    public Product getProductById(@PathVariable Long productId){
        return productService.getProductById(productId);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> CreateProduct(@RequestBody Product product) {
        try {
            Product addedProduct = productService.addProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Product eklenirken bir hata oluştu.");
        }
    }
    @PutMapping("/{productId}")
    public Product UpdateProduct(@RequestBody Product newProduct, @PathVariable Long productId){

        return productService.updateProduct(newProduct,productId);
    }
    @DeleteMapping("{productId}")
    public void DeleteProduct(@PathVariable Long productId){
        productService.deleteProductById(productId);

    }
}
