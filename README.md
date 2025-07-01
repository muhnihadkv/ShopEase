# ShopEase: Smart Shopping Hub 🛍️

ShopEase is a simple e-commerce application built using Spring Boot that allows users to browse products, add them to a cart, and proceed to checkout. It incorporates modern technologies for a smooth and secure shopping experience.

## 🚀 Features

- ✅ Spring Boot with Microservices – Modular and scalable architecture  
- ✅ Reactive Programming with WebFlux – For efficient and non-blocking operations  
- ✅ JWT Authentication – Secure access control for users  
- ✅ Stripe Integration – Smooth and secure payment processing  
- ✅ MySQL Database – Reliable and efficient data management
- ✅ Dockerized Microservices – Easy containerized deployment

## 🏗️ Microservices Architecture

ShopEase is built with a microservices architecture consisting of the following services:

1. User Service – Handles user authentication and profile management.  
2. Product Service – Manages product listings, cart, and order management.  
3. Payment Service – Handles Coupon feature & payment processing via Stripe.  
4. API Gateway – Routes requests to the appropriate microservice.  
5. Service Registry – Manages service discovery.

## 🛠️ Tech Stack

- Backend: Spring Boot, Spring WebFlux, Spring Security  
- Database: MySQL (Separate DB for each service)  
- Security: JWT (JSON Web Tokens)  
- Payment Gateway: Stripe  
- Service Discovery: Spring Cloud Netflix Eureka  
- API Gateway: Spring Cloud Gateway
- Containerization: Docker & Docker Compose

---

## 📦 Docker Setup

### 1️⃣ Prerequisites

* Docker and Docker Compose installed on your system.
* Stripe CLI installed for local webhook testing.

### 2️⃣ Build Docker Images

Run the following command from the root of the project:

```bash
docker-compose build
```

### 3️⃣ Start the Containers

```bash
docker-compose up
```

This will start all the services:

* MySQL Database (Port: 3307)
* Eureka Server (Port: 8761)
* API Gateway (Port: 8080)
* User Service (Port: 9191)
* Product Service (Port: 9192)
* Payment Service (Port: 9193)

### 4️⃣ Verify Eureka Dashboard

Visit:

```
http://localhost:8761
```

You should see all microservices registered.

### 5️⃣ Stripe Webhook (Local Testing)

Run the Stripe CLI to listen for webhook events:

```bash
stripe listen --forward-to http://localhost:9193/payment/webhook
```

This will forward Stripe events to your local API Gateway.

### 6️⃣ Access the Application

All requests should go through the API Gateway on port 8080:

```
http://localhost:8080
```

---

## 🛠️ Manual Setup (Without Docker)

If you prefer to run the services manually, follow these steps:

### ✅ Database Setup

Ensure MySQL is running and create the following databases:

```sql
CREATE DATABASE user_db;
CREATE DATABASE product_db;
CREATE DATABASE payment_db;
```

Update the `application.properties` or `application.yml` files in each service with your local MySQL credentials.


## ⚙️ Running Services (Without Docker)

Start the Service Registry:

```bash
cd Service-Registry
mvn spring-boot:run
```

Start the other services:

```bash
cd User && mvn spring-boot:run
cd Product && mvn spring-boot:run
cd Payment && mvn spring-boot:run
cd API-Gateway && mvn spring-boot:run
```

Access the application via:

```
http://localhost:8080
```

---

## 📌 Future Improvements

- Implementing a recommendation system for personalized product suggestions
- Adding a review and rating system for products
- Adding a front-end using React

## 🤝 Contributing

Feel free to fork this repository and submit pull requests with improvements! 🚀

## 📜 License

This project is for educational purposes only.
