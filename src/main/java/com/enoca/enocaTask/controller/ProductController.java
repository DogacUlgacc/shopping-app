package com.enoca.enocaTask.controller;

import com.enoca.enocaTask.dto.ProductDto;
import com.enoca.enocaTask.entity.Product;
import com.enoca.enocaTask.service.ProductService;
import lombok.Data;
import org.springframework.data.domain.Page;
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
    public Page<Product> getAllProduct(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size) {
        return productService.getAllProducts(page, size);
    }

    @GetMapping("/{productId}")
    public Product getProductById(@PathVariable Long productId) {
        return productService.getProductById(productId);
    }

    @GetMapping("/sort")
    public List<ProductDto> sortProductWithLimit(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false, defaultValue = "1") int limit
    ) {
        return productService.getProductsWithRequestParam(sort, limit);
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
    public Product updateProduct(@RequestBody ProductDto newProduct, @PathVariable Long productId) {
        return productService.updateProduct(newProduct, productId); 
    }


    @DeleteMapping("/delete/{productId}")
    public void deleteProduct(@PathVariable Long productId) {
        productService.deleteProductById(productId);
        productService.deleteProductById(productId);

    }
}
