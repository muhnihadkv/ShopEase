package com.ShopEase.Payment.controllers;

import com.ShopEase.Payment.dtos.PaymentRequest;
import com.ShopEase.Payment.dtos.StripeResponse;
import com.ShopEase.Payment.entities.Payment;
import com.ShopEase.Payment.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/checkout")
    public Mono<StripeResponse> checkOut(@RequestBody PaymentRequest paymentRequest){
        return paymentService.checkoutProducts(paymentRequest);
    }

    @PostMapping("/webhook")
    public Mono<String>  handleStripeEvent(@RequestBody String payload,
                                                    @RequestHeader("Stripe-Signature") String sigHeader){
        return paymentService.handleStripeEvent(payload, sigHeader);
    }

    @GetMapping("/getStatus/{paymentId}")
    public ResponseEntity<Payment> getStatus(@PathVariable int paymentId){
        Payment payment = paymentService.getPayment(paymentId);
        return ResponseEntity.ok(payment);
    }
}
