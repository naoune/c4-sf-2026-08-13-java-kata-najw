# Carrefour Loyalty Kata

## Overview

This project implements a loyalty points management system.

Customers can:

- Earn loyalty points
- Spend loyalty points
- Convert points into vouchers
- Donate points to charity

Points have an expiration date and the oldest points are consumed first (FIFO).

---

## Technologies

- Java 25
- Spring Boot
- Spring Data JPA
- Apache Kafka
- H2 Database
- Maven
- JUnit 5
- Lombok

---

## Assumptions

As the user stories did not provide detailed acceptance criteria, the following assumptions were made :

- Loyalty points have an expiration date.
- The expiration period is assumed to be one year.
- Points are consumed using FIFO (oldest points first).
- Customers receive notifications before point expiration.
- Voucher creation consumes loyalty points.
- Donations consume loyalty points.

---

## Architecture
The project follows a simple layered architecture adapted to the kata scope.

com.najwa.kata.loyalty

├── controller
├── kafka
├── model
├── repository
├── service
└── LoyaltyKataApplication

## REST Endpoints

POST /loyalty/{accountId}/earn?points=100

POST /loyalty/{accountId}/spend?points=50

POST /loyalty/{accountId}/voucher?points=500

POST /loyalty/{accountId}/donation?points=200

GET /loyalty/{accountId}

## Kafka Events

POINTS_EARNED

POINTS_SPENT

VOUCHER_CREATED

POINTS_DONATED

POINTS_EXPIRED

## Run Application

Build:

mvnw.cmd clean install

Run:

mvnw.cmd spring-boot:run

Run tests:

mvnw.cmd test