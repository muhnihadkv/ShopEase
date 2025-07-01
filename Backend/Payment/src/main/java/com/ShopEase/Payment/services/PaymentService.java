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
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
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

    public Mono<StripeResponse> checkoutProducts(PaymentRequest paymentRequest){
        return Mono.fromCallable(()-> {
            Stripe.apiKey = secretKey;
            long price = paymentRequest.getPrice().longValue();
            long discount = paymentRequest.getDiscount().longValue();

            if (discount > price) {
                throw new RuntimeException("Discount cannot be greater than price.");
            }

            SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams
                    .LineItem.PriceData.ProductData.builder()
                    .setName(paymentRequest.getProductName()).build();

            SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
                    .setUnitAmount((price -discount) * 100)
                    .setCurrency(paymentRequest.getCurrency() == null ? "USD" : paymentRequest.getCurrency())
                    .setProductData(productData)
                    .build();

            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                    .setQuantity(paymentRequest.getQuantity())
                    .setPriceData(priceData).build();

            SessionCreateParams params = SessionCreateParams.builder()
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("http://localhost:8080/payment/success")
                    .setCancelUrl("http://localhost:8080/payment/cancel")
                    .setExpiresAt(System.currentTimeMillis()/1000+1800)
                    .addLineItem(lineItem)
                    .putAllMetadata(Map.of("orderId", String.valueOf(paymentRequest.getOrderId())))
                    .build();

            Session session;
            try {
                session = Session.create(params);
                logger.info("Checkout session created for order: {}", paymentRequest.getOrderId());
            } catch (StripeException e) {
                throw new RuntimeException(e.getMessage());
            }

            return StripeResponse.builder()
                    .sessionId(session.getId())
                    .status(Status.SUCCESS)
                    .message("Payment Session Created")
                    .sessionUrl(session.getUrl())
                    .build();
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public Payment getPayment(int paymentId) {
        return paymentRepository.findById(paymentId).orElseThrow(()-> new NoSuchElementException("Payment not found"));
    }

    public void addNewPayment(int orderId, Status status, long amount){
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setStatus(status);
        payment.setAmount(BigDecimal.valueOf(amount));
        paymentRepository.save(payment);
    }

    public Mono<String> handleStripeEvent(String payload, String sigHeader) {
        return Mono.fromCallable(()-> {
            Event event;
            if (sigHeader == null)
                return "";
            try {
                event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            } catch (SignatureVerificationException e) {
                logger.info("Error while validating signature");
                return "Invalid Signature";
            }

            EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
            StripeObject stripeObject;
            if (dataObjectDeserializer.getObject().isPresent()) {
                stripeObject = dataObjectDeserializer.getObject().get();
            } else {
                logger.info("deserialization failed!!!");
                return "deserialization failed";
            }

            Session session = (Session) stripeObject;
            int orderId = Integer.parseInt(session.getMetadata().get("orderId"));
            long amount = session.getAmountTotal() / 100;
            switch (event.getType()) {
                case "checkout.session.completed":
                    logger.info("payment for {} with orderId: {} succeeded", amount, orderId);
                    addNewPayment(orderId, Status.SUCCESS, amount);
                    updateOrderSuccess(orderId);
                    break;

                case "checkout.session.async_payment_failed":
                    logger.info("payment for {} with orderId: {} failed", amount, orderId);
                    addNewPayment(orderId, Status.FAILED, amount);
                    updateOrderFailure(orderId);
                    break;

                default:
                    logger.warn("unhandled event type: {}", event.getType());
                    break;
            }

            return "success";
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
