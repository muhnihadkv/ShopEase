package com.ShopEase.Product.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "`order`")
@Data
@AllArgsConstructor
@NoArgsConstructor
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
}
