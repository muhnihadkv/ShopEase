package com.ShopEase.Product.controllers;

import com.ShopEase.Product.dtos.CartDto;
import com.ShopEase.Product.dtos.CartQuantityDto;
import com.ShopEase.Product.entities.Cart;
import com.ShopEase.Product.services.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addCart(@RequestBody CartDto cartDto,
                                        HttpServletRequest request){
        Cart cart = cartService.addCart(cartDto, request);
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/getByUserId")
    public ResponseEntity<List<Cart>> getByUserId(HttpServletRequest request){
        List<Cart> carts = cartService.getByUserId(request);
        return ResponseEntity.ok(carts);
    }

    @PutMapping("/update")
    public ResponseEntity<Cart> updateCart(@RequestBody CartQuantityDto cartQuantityDto){
        Cart cart = cartService.updateCart(cartQuantityDto);
        return ResponseEntity.ok(cart);

    }

    @DeleteMapping("deleteById/cartId")
    public ResponseEntity<String> deleteById(@PathVariable int cartId){
        cartService.deleteById(cartId);
        return ResponseEntity.ok("deleted successfully");
    }
}
