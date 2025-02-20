package com.ShopEase.Product.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartId;
    private int userId;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "productId", referencedColumnName = "productId", nullable = false)
    private Product product;

    private int quantity;
}
