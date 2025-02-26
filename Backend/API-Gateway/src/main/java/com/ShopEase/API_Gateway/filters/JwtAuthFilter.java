package com.ShopEase.API_Gateway.filters;

import com.ShopEase.API_Gateway.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RouteValidator routeValidator;

    public JwtAuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (routeValidator.isSecured.test(exchange.getRequest())){
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                    throw new IllegalArgumentException("missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0).substring(7);

                try{
                    jwtUtil.validateToken(authHeader);
                }catch (Exception e){
                    throw new IllegalArgumentException("Unauthorized access: Invalid token");
                }
                String role = jwtUtil.extractRole(authHeader);
                if (exchange.getRequest().getURI().getPath().endsWith("/admin") && !role.equals("ADMIN")){
                    throw new IllegalArgumentException("Access to this is restricted");
                }
            }
            return chain.filter(exchange);
        });
    }
    public static class Config{

    }
}
