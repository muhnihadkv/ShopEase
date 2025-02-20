package com.ShopEase.User.services;

import com.ShopEase.User.dtos.LoginDto;
import com.ShopEase.User.entities.Roles;
import com.ShopEase.User.entities.User;
import com.ShopEase.User.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public User registerUser(LoginDto loginDto) {
        User user = new User();
        user.setEmail(loginDto.getEmail());
        user.setPassword(loginDto.getPassword());
        user.setName(loginDto.getName());
        user.setRole(Roles.USER);
        userRepository.save(user);
        return user;
    }

    public String loginUser(LoginDto loginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );

        User user = userRepository.findByEmail(loginDto.getEmail());
        return jwtService.generateToken(user);
    }

    public int getUserId(String token){
        return jwtService.extractUserId(token);
    }
}
