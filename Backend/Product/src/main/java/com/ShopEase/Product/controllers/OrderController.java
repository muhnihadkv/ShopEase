package com.ShopEase.Product.controllers;

import com.ShopEase.Product.dtos.OrderDto;
import com.ShopEase.Product.entities.Order;
import com.ShopEase.Product.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestBody OrderDto orderDto,
                                             @RequestHeader("Authorization") String authHeader){
        Order order = orderService.createOrder(orderDto, authHeader);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/getByUserId")
    public ResponseEntity<List<Order>> getByUserId(@RequestHeader("Authorization") String authHeader){
        List<Order> orders = orderService.getByUserId(authHeader);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("getOrder/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable int orderId){
        Order order = orderService.getOrder(orderId);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/paymentSuccess/{orderId}")
    public void paymentSuccess(@PathVariable int orderId){
        orderService.paymentSuccess(orderId);
    }

    @PutMapping("/paymentFailed/{orderId}")
    public void paymentFailed(@PathVariable int orderId){
        orderService.paymentFailed(orderId);
    }
}
