package com.ShopEase.Product.controllers;

import com.ShopEase.Product.dtos.OrderDto;
import com.ShopEase.Product.entities.Order;
import com.ShopEase.Product.services.OrderService;
import jakarta.servlet.http.HttpServletRequest;
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
                                             HttpServletRequest request){
        Order order = orderService.createOrder(orderDto, request);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/getByUserId")
    public ResponseEntity<List<Order>> getByUserId(HttpServletRequest request){
        List<Order> orders = orderService.getByUserId(request);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("getOrder/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable int orderId){
        Order order = orderService.getOrder(orderId);
        return ResponseEntity.ok(order);
    }
}
