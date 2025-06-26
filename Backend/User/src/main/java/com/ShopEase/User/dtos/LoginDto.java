package com.ShopEase.User.dtos;


public class LoginDto {
    private String email;
    private  String password;
    private String name;

    public void setEmail(String email){
        this.email=email;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
