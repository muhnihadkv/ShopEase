package com.ShopEase.Product.services;

import com.ShopEase.Product.dtos.CartDto;
import com.ShopEase.Product.dtos.CartQuantityDto;
import com.ShopEase.Product.entities.Cart;
import com.ShopEase.Product.repositories.CartRepository;
import com.ShopEase.Product.repositories.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final WebClient.Builder builder;

    String url = "http://USER-SERVICE/auth/getUserIdFromToken?token=";

    public CartService(CartRepository cartRepository, ProductRepository productRepository, WebClient.Builder builder) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.builder = builder;
    }

    public Cart addCart(CartDto cartDto, HttpServletRequest request) {
        Cart cart = new Cart();
        cart.setProduct(productRepository.findById(cartDto.getProductId()).orElse(null));
        cart.setUserId(getUserId(request));
        cart.setQuantity(cartDto.getQuantity());
        cartRepository.save(cart);
        return cart;
    }

    private int getUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return builder.build().get()
                .uri(url + token).retrieve().bodyToMono(Integer.class).block();
    }

    public List<Cart> getByUserId(HttpServletRequest request) {
        return cartRepository.findAllByUserId(getUserId(request));
    }

    public Cart updateCart(CartQuantityDto cartQuantityDto) {
        Cart cart = cartRepository.findById(cartQuantityDto.getCartId()).orElse(null);
        cart.setQuantity(cartQuantityDto.getQuantity());
        if(cartQuantityDto.getQuantity()==0){
            deleteById(cartQuantityDto.getCartId());
        }
        else {
            cartRepository.save(cart);
        }
        return  cart;
    }

    public void deleteById(int cartId) {
        cartRepository.deleteById(cartId);
    }
}
