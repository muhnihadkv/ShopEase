package com.ShopEase.Product.controllers;

import com.ShopEase.Product.dtos.ProductDto;
import com.ShopEase.Product.entities.Product;
import com.ShopEase.Product.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(@RequestBody ProductDto productDto){
        Product product = productService.addProduct(productDto);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/update")
    public ResponseEntity<Product> updateProduct(@RequestBody ProductDto productDto){
        Product product = productService.updateProduct(productDto);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Product>> getAll(){
        List<Product> products = productService.getAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/getById/{productId}")
    public ResponseEntity<Product> getById(@PathVariable int productId){
        Product product = productService.getById(productId);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("deleteById/{productId}")
    public ResponseEntity<String> deleteById(@PathVariable int productId){
        productService.deleteById(productId);
        return ResponseEntity.ok("deleted successfully");
    }
}
