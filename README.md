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

### 2️⃣ Set up MySQL Databases

Ensure MySQL is installed and running. Create separate databases for each microservice:

```sql
CREATE DATABASE user_db;
CREATE DATABASE product_db;
CREATE DATABASE payment_db;
```

Update the `application.properties` file in each microservice with its respective database credentials.

🔹 Example (`src/main/resources/application.properties`)

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/user_db
spring.datasource.username=root
spring.datasource.password=yourpassword
```

Repeat this for product-service and payment-service with their respective database names.

### 3️⃣ Configure Security Settings

Update the User Service (`src/main/resources/application.properties`) with JWT security settings:

```properties
security.jwt.secret-key=your_jwt_secret_key
security.jwt.expiration-time=3600000  # Token expiry in milliseconds (1 hour)
```

### 4️⃣ Set up Stripe Account

1. Create an account on [Stripe](https://stripe.com/).
2. Get your Secret API Key from the Stripe Dashboard (Developers -> API Keys).
3. Set up webhooks for payment events.

🔹 Update Payment Service `src/main/resources/application.properties`

```properties
stripe.secret-key=your_stripe_secret_key
stripe.webhook.secret=your_webhook_secret
```

For webhook setup, refer to the [Stripe Webhooks Documentation](https://stripe.com/docs/webhooks).

- If using localhost, you can use the [Stripe CLI](https://stripe.com/docs/stripe-cli) to forward webhooks.
- If deploying in the cloud, set up webhooks in the Stripe Dashboard.

### 5️⃣ Run the Services

Start the Service Registry first:

```bash
cd service-registry
mvn spring-boot:run
```

Then, start the User Service, Product Service, and Payment Service:

```bash
cd user-service
mvn spring-boot:run

cd ../product-service
mvn spring-boot:run

cd ../payment-service
mvn spring-boot:run
```

Finally, start the API Gateway:

```bash
cd api-gateway
mvn spring-boot:run
```

### 6️⃣ Access the Application

All requests should go through the API Gateway on port 8080:

```
http://localhost:8080
```

## 📌 Future Improvements

- Implementing a recommendation system for personalized product suggestions
- Adding a review and rating system for products
- Adding a front-end using React

## 🤝 Contributing

Feel free to fork this repository and submit pull requests with improvements! 🚀

## 📜 License

This project is for educational purposes only.
