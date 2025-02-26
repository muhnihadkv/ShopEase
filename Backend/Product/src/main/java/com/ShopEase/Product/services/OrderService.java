package com.ShopEase.Product.services;

import com.ShopEase.Product.dtos.OrderDto;
import com.ShopEase.Product.entities.Order;
import com.ShopEase.Product.entities.Status;
import com.ShopEase.Product.repositories.OrderRepository;
import com.ShopEase.Product.repositories.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
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

    public int getUserId(HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        return builder.build().get().uri(url + token)
                .retrieve().bodyToMono(Integer.class).block();
    }

    public Order createOrder(OrderDto orderDto, HttpServletRequest request) {
        int userId = getUserId(request);

        Order order = new Order();
        order.setUserId(userId);
        order.setProduct(productRepository.findById(orderDto.getProductId()).orElse(null));
        order.setStatus(Status.PENDING);
        order.setQuantity(orderDto.getQuantity());
        orderRepository.save(order);

        return order;
    }

    public List<Order> getByUserId(HttpServletRequest request) {
        int userId = getUserId(request);
        return orderRepository.findAllByUserId(userId);
    }

    public Order getOrder(int orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }
}
