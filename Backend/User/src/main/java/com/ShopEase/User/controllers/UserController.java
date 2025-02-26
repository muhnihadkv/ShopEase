package com.ShopEase.User.controllers;

import com.ShopEase.User.dtos.LoginDto;
import com.ShopEase.User.entities.User;
import com.ShopEase.User.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody LoginDto loginDto){
        User user = userService.registerUser(loginDto);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginDto loginDto){
        String token = userService.loginUser(loginDto);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/getUserIdFromToken")
    public int getUserId(@RequestParam String token){
        return userService.getUserId(token);
    }

}
