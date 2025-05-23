# XBookstoreAPI

## Overview
XBookstoreAPI is a RESTful backend application built for the SCOSC022W Client-Server Architectures coursework. It provides a bookstore management system, allowing users to manage books, authors, customers, shopping carts, and orders via HTTP requests. The API uses in-memory storage and generates UUIDs (e.g., `5aa4e8a9-6fd3-44fc-afc6-e39aacf6e665`) for resource identification. It supports CRUD operations, exception handling, and CORS for client compatibility.

## Technologies
- **Java EE 8 Web**: Backend framework for RESTful services.
- **JAX-RS (Jersey)**: Implementation for building REST APIs.
- **Apache Tomcat/TomEE**: Servlet container for deployment.
- **Maven**: Build and dependency management.
- **In-Memory Storage**: ArrayList-based storage for books, customers, carts, and orders.
- **NetBeans**: IDE for development and deployment.
- **Postman**: API testing with provided collection.

## Prerequisites
- **Java 8**: JDK for development.
- **Maven 3.6+**: For building the project.
- **Apache Tomcat 9.x**: For deployment (compatible with Java EE 8).
- **NetBeans 12+**: Recommended IDE.
- **Postman**: For testing API endpoints.

## Setup Instructions
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/<your-username>/XBookstoreAPI.git
   cd XBookstoreAPI
