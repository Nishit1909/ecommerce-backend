# E-Commerce Backend

A production-style REST API for an e-commerce platform built with Spring Boot, JPA, Hibernate, and PostgreSQL.

## Features
- Product management with image upload and search
- Order placement with stock management
- Custom JPQL search across name, brand, category, description
- DTO-based API design using Java Records
- JPA entity relationships (OneToMany, ManyToOne)

## Tech Stack
- Java, Spring Boot, Spring Data JPA, Hibernate
- PostgreSQL
- Lombok, Maven

## API Endpoints
- GET /api/products - Get all products
- GET /api/product/{id} - Get product by ID
- POST /api/product - Add product
- DELETE /api/product/{id} - Delete product
- GET /api/products/search?keyword= - Search products
- POST /api/orders/place - Place an order
- GET /api/orders - Get all orders

## Setup
- Configure your PostgreSQL credentials in application.properties
- Run with Maven: mvn spring-boot:run