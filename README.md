# ShopEase: Smart Shopping Hub 🛍️

ShopEase is a simple e-commerce application built using Spring Boot that allows users to browse products, add them to a cart, and proceed to checkout. It incorporates modern technologies for a smooth and secure shopping experience.

## 🚀 Features

- ✅ Spring Boot with Microservices – Modular and scalable architecture  
- ✅ Reactive Programming with WebFlux – For efficient and non-blocking operations  
- ✅ JWT Authentication – Secure access control for users  
- ✅ Stripe Integration – Smooth and secure payment processing  
- ✅ MySQL Database (Separate DB for each microservice) – Reliable and efficient data management  

## 🏗️ Microservices Architecture

ShopEase is built with a microservices architecture consisting of the following services:

1. User Service – Handles user authentication and profile management.  
2. Product Service – Manages product listings, cart, and order management.  
3. Payment Service – Handles payment processing via Stripe.  
4. API Gateway – Routes requests to the appropriate microservice.  
5. Service Registry – Manages service discovery.

## 🛠️ Tech Stack

- Backend: Spring Boot, Spring WebFlux, Spring Security  
- Database: MySQL (Separate DB for each service)  
- Security: JWT (JSON Web Tokens)  
- Payment Gateway: Stripe  
- Service Discovery: Spring Cloud Netflix Eureka  
- API Gateway: Spring Cloud Gateway  

## 📌 Installation & Setup

### 1️⃣ Clone the repository

```bash
git clone https://github.com/yourusername/shopease.git
cd shopease
```
