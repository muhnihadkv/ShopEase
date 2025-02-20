package com.ShopEase.Product.services;

import com.ShopEase.Product.dtos.ProductDto;
import com.ShopEase.Product.entities.Product;
import com.ShopEase.Product.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product addProduct(ProductDto productDto) {
        Product product = new Product();
        product.setProductName(productDto.getProductName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());
        
        productRepository.save(product);
        return product;
    }

    public Product updateProduct(ProductDto productDto) {
        Product product = productRepository.findById(productDto.getProductId()).orElse(null);
        product.setProductName(productDto.getProductName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());
        productRepository.save(product);
        return product;
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product getById(int productId) {
        return productRepository.findById(productId).orElse(null);
    }

    public void deleteById(int productId) {
        productRepository.deleteById(productId);
    }
}
