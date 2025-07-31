# Order Management & Loggings Microservices

This is a two-microservice application built with **Java 21** and **Spring Boot 3.5**. 
The system manages customer orders and logs the processed orders to a logging service.

---

## Microservices Overview

### order-management

A REST microservice for managing customer orders.

**Features:**
- `POST /orders` – Create a new order
- `GET /orders/{id}` – Retrieve an order by ID
- `PUT /orders/{id}` – Update an existing order
- `DELETE /orders/{id}` – Delete an order
- Scheduled job detects unprocessed orders and sends them to `loggings-service`
- Exception handlers for custom error responses
- Uses PostgreSQL as the data store
- Uses MapStruct for mapping
- Includes unit tests with JUnit 5

---

### loggings-api

A REST microservice that receives processed orders and stores them in MongoDB.

**Features:**
- `POST /logs` – Receive and persist log entries
- Exception handlers for consistent error responses
- Uses MongoDB as the data store
- Uses ModelMapper for mapping

---

## Tech Stack

| Layer                | Tech Used                           |
|---------------------|-------------------------------------|
| Language            | Java 21                             |
| Framework           | Spring Boot 3.5                     |
| API Communication   | REST                                |
| Databases           | PostgreSQL (order-management)       |
|                     | MongoDB (loggings-service)          |
| Object Mapping      | MapStruct (order-management)        |
|                     | ModelMapper (loggings-service)      |
| Testing             | JUnit 5  (order-management)         |
| Testing             | Integration Test (loggings-service) |
| Packaging           | JARs                                |
| Deployment          | Docker, Docker Compose              |

---

## Docker & Docker Compose

Both services are containerized and orchestrated via `docker-compose`, 
which also includes PostgreSQL and MongoDB containers.

### Prerequisites

- Docker
- Docker Compose

### Running the Services

```bash
cd order-application

# Start the services

docker compose up --build

#Once the server is running locally, open your browser and navigate to:  

http://localhost:9090/swagger-ui/index.html