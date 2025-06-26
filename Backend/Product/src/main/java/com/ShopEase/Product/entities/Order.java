package com.ShopEase.Product.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "`order`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    private int userId;

    @ManyToOne
    @JoinColumn(name = "productId",referencedColumnName = "productId",nullable = false)
    private Product product;

    private int quantity;
    private Status status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updatedAt;

    // No-argument constructor
    public Order() {
    }

//    // All-argument constructor
//    public Order(int orderId, int userId, Product product, int quantity, Status status, LocalDate createdAt, LocalDate updatedAt) {
//        this.orderId = orderId;
//        this.userId = userId;
//        this.product = product;
//        this.quantity = quantity;
//        this.status = status;
//        this.createdAt = createdAt;
//        this.updatedAt = updatedAt;
//    }

    // Getters and Setters

    public int getOrderId() {
        return orderId;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
