package com.ShopEase.Product.services;

import com.ShopEase.Product.dtos.OrderDto;
import com.ShopEase.Product.entities.Order;
import com.ShopEase.Product.entities.Product;
import com.ShopEase.Product.entities.Status;
import com.ShopEase.Product.repositories.OrderRepository;
import com.ShopEase.Product.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder builder;
    private final ProductRepository productRepository;

    String url = "http://USER-SERVICE/auth/getUserIdFromToken?token=";

    public OrderService(OrderRepository orderRepository, WebClient.Builder builder, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.builder = builder;
        this.productRepository = productRepository;
    }

    public int getUserId(String authHeader){
        String token = authHeader.substring(7);
        return builder.build().get().uri(url + token)
                .retrieve().bodyToMono(Integer.class).block();
    }

    public Order createOrder(OrderDto orderDto, String authHeader) {
        int userId = getUserId(authHeader);

        Order order = new Order();
        Product product = productRepository.findById(orderDto.getProductId())
                .orElseThrow(()-> new NoSuchElementException("Product not found"));
        order.setUserId(userId);
        order.setProduct(product);
        order.setStatus(Status.PENDING);
        order.setQuantity(orderDto.getQuantity());
        if(product.getStock()-orderDto.getQuantity()>=0) {
            product.setStock(product.getStock()-orderDto.getQuantity());
            productRepository.save(product);
        }else if(product.getStock()==0){
            throw new IllegalArgumentException(product.getProductName()+ " is out of stock");
        }else{
            throw new IllegalArgumentException("Only " + product.getStock()+" stock of "+product.getProductName()+ " left!!!");
        }
        orderRepository.save(order);

        return order;
    }

    public List<Order> getByUserId(String authHeader) {
        int userId = getUserId(authHeader);
        return orderRepository.findAllByUserId(userId);
    }

    public Order getOrder(int orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    public void paymentSuccess(int orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        order.setStatus(Status.PLACED);
        orderRepository.save(order);
    }

    public void paymentFailed(int orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        order.setStatus(Status.CANCELLED);
        orderRepository.save(order);

        Product product = order.getProduct();
        product.setStock(product.getStock()  + order.getQuantity());
        productRepository.save(product);
    }
}
