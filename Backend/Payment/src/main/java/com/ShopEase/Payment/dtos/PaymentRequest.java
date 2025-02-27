package com.ShopEase.Payment.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private String productName;
    private long price;
    private long quantity;
    private String currency;
    private int orderId;
}
