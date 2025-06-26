package com.ShopEase.Product.entities;

import jakarta.persistence.*;

@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartId;
    private int userId;

    @ManyToOne
    @JoinColumn(name = "productId", referencedColumnName = "productId", nullable = false)
    private Product product;

    private int quantity;

    // No-argument constructor
    public Cart() {
    }

    // All-arguments constructor
    public Cart(int cartId, int userId, Product product, int quantity) {
        this.cartId = cartId;
        this.userId = userId;
        this.product = product;
        this.quantity = quantity;
    }

    // Getters and Setters

    public int getCartId() {
        return cartId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
