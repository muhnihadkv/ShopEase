package com.ShopEase.Payment.services;

import com.ShopEase.Payment.dtos.PaymentRequest;
import com.ShopEase.Payment.dtos.StripeResponse;
import com.ShopEase.Payment.entities.Payment;
import com.ShopEase.Payment.entities.Status;
import com.ShopEase.Payment.repositories.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.NoSuchElementException;


@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final WebClient.Builder builder;
    private final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Value("${stripe.secret-key}")
    private String secretKey;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    private String baseUrl = "http://PRODUCT-SERVICE/order";

    public PaymentService(PaymentRepository paymentRepository, WebClient.Builder builder) {
        this.paymentRepository = paymentRepository;
        this.builder = builder;
    }

    public void updateOrderSuccess(int orderId){
        builder.baseUrl(baseUrl).build().put().uri("/paymentSuccess/{orderId}",orderId)
                .retrieve().bodyToMono(Void.class).block();
    }

    public void updateOrderFailure(int orderId){
        builder.baseUrl(baseUrl).build().put().uri("/paymentFailed/{orderId}",orderId)
                .retrieve().bodyToMono(Void.class).block();
    }

    public StripeResponse checkoutProducts(PaymentRequest paymentRequest){
        Stripe.apiKey=secretKey;
        SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams
                .LineItem.PriceData.ProductData.builder()
                .setName(paymentRequest.getProductName()).build();

        SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
                .setUnitAmount(paymentRequest.getPrice())
                .setCurrency(paymentRequest.getCurrency() == null ? "USD" : paymentRequest.getCurrency())
                .setProductData(productData)
                .build();

        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setQuantity(paymentRequest.getQuantity())
                .setPriceData(priceData).build();

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.PAYPAL)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/payment/success")
                .setCancelUrl("http://localhost:8080/payment/cancel")
                .addLineItem(lineItem)
                .putAllMetadata(Map.of("orderId", String.valueOf(paymentRequest.getOrderId())))
                .build();

        Session session;
        try {
            session = Session.create(params);
            logger.info("Checkout session created for order: {}",paymentRequest.getOrderId());
        } catch (StripeException e) {
            throw new RuntimeException(e.getMessage());
        }

        return StripeResponse.builder()
                .sessionId(session.getId())
                .status(Status.SUCCESS)
                .message("Payment Session Created")
                .sessionUrl(session.getUrl())
                .build();
    }

    public Payment getPayment(int paymentId) {
        return paymentRepository.findById(paymentId).orElseThrow(()-> new NoSuchElementException("Payment not found"));
    }

    public void addNewPayment(int orderId, Status status, long amount){
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setStatus(status);
        payment.setAmount(amount);
        paymentRepository.save(payment);
    }

    public String handleStripeEvent(String payload, String sigHeader) {
        Event event;
        if (sigHeader==null)
            return "";
        try{
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            logger.info("Error while validating signature");
            return "Invalid Signature";
        }

        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject;
        if (dataObjectDeserializer.getObject().isPresent()){
            stripeObject = dataObjectDeserializer.getObject().get();
        }else {
            logger.info("deserialization failed!!!");
            return "deserialization failed";
        }

        PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
        int orderId = Integer.parseInt(paymentIntent.getMetadata().get("orderId"));
        long amount = paymentIntent.getAmount();
        switch (event.getType()){
            case "payment_intent.succeeded":
                logger.info("payment for {} with orderId: {} succeeded", amount, orderId);
                addNewPayment(orderId, Status.SUCCESS, amount);
                updateOrderSuccess(orderId);
                break;

            case "payment_intent.payment_failed":
                logger.info("payment for {} with orderId: {} failed", amount, orderId);
                addNewPayment(orderId, Status.FAILED, amount);
                updateOrderFailure(orderId);
                break;

            case "payment_intent.canceled":
                logger.info("payment for {} with orderId: {} canceled", amount, orderId);
                addNewPayment(orderId, Status.CANCELED, amount);
                updateOrderFailure(orderId);
                break;

            default:
                logger.warn("unhandled event type: {}", event.getType());
                break;
        }

        return "success";
    }
}
