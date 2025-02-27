package com.ShopEase.Payment.controllers;

import com.ShopEase.Payment.dtos.PaymentRequest;
import com.ShopEase.Payment.dtos.StripeResponse;
import com.ShopEase.Payment.entities.Payment;
import com.ShopEase.Payment.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkOut(@RequestBody PaymentRequest paymentRequest){
        StripeResponse response = paymentService.checkoutProducts(paymentRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeEvent(@RequestBody String payload,
                                                    @RequestHeader("Stripe-Signature") String sigHeader){
        String response = paymentService.handleStripeEvent(payload, sigHeader);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getStatus/{paymentId}")
    public ResponseEntity<Payment> getStatus(@PathVariable int paymentId){
        Payment payment = paymentService.getPayment(paymentId);
        return ResponseEntity.ok(payment);
    }
}
