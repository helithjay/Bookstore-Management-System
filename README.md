XBookstoreAPI
XBookstoreAPI is a RESTful web service for managing a bookstore, developed as part of the SCOSC022W Client-Server Architectures coursework. It provides endpoints to manage books, authors, customers, carts, and orders, using in-memory storage for simplicity. The API is built with Java EE 8 and JAX-RS (Jersey) and deployed on Apache Tomcat/TomEE.
Features

Books: Create, read, update, and delete books (POST /books, GET /books, GET /books/{id}, PUT /books/{id}, DELETE /books/{id}).
Authors: Create and retrieve authors (POST /authors, GET /authors, GET /authors/{id}).
Customers: Register customers (POST /customers, GET /customers/{id}).
Carts: Add/remove items to/from a customer’s cart (POST /customers/{customerId}/cart/items, GET /customers/{customerId}/cart, DELETE /customers/{customerId}/cart/items/{bookId}).
Orders: Create and retrieve orders (POST /customers/{customerId}/orders, GET /customers/{customerId}/orders, GET /customers/{customerId}/orders/{orderId}).
Error handling for invalid inputs, insufficient stock, and resource not found (e.g., 404, 400).
In-memory storage using ArrayList for data persistence during runtime.
CORS support for integration with frontends (e.g., React on http://localhost:3000).

Technologies

Java EE 8: Web profile for RESTful services.
JAX-RS (Jersey 2.34): REST API implementation.
Maven: Dependency management and build tool.
Apache Tomcat/TomEE: Servlet container (tested with Tomcat 9.x).
NetBeans: IDE for development and deployment.
Postman: API testing.

Prerequisites

Java 8: JDK for development and runtime.
Maven 3.6+: For building the project.
Apache Tomcat 9.x: Servlet container (TomEE also supported).
NetBeans 12+: Recommended IDE (optional, as Maven can be run standalone).
Postman: For testing API endpoints.
Git (for cloning the repository).

Setup and Running

Clone the Repository:
git clone https://github.com/your-username/XBookstoreAPI.git
cd XBookstoreAPI


Configure Tomcat:

Install Tomcat 9.x from https://tomcat.apache.org/.
In NetBeans, add Tomcat via Tools > Servers > Add Server.
Ensure port 8080 is free (edit Tomcat/conf/server.xml if needed, e.g., change to 8081).


Build the Project:

Open the project in NetBeans (File > Open Project) or use Maven:mvn clean install


This generates target/XBookstoreAPI-1.0-SNAPSHOT.war.


Deploy to Tomcat:

In NetBeans, right-click the project > Run to deploy to Tomcat.
Or, copy target/XBookstoreAPI-1.0-SNAPSHOT.war to Tomcat/webapps and start Tomcat:Tomcat/bin/startup.bat  # Windows
Tomcat/bin/startup.sh   # Linux/macOS


The API will be available at http://localhost:8080/XBookstoreAPI/api.


Verify Deployment:

Open http://localhost:8080/XBookstoreAPI/api/books in a browser.
Expected: JSON array of books (e.g., [{"id": "5aa4e8a9-6fd3-44fc-afc6-e39aacf6e665", "title": "The Lord of the Rings", ...}]).



Testing with Postman

Import Collection:

Use the provided XBookstoreAPI.postman_collection.json (in the repository root).
Import into Postman: File > Import > Choose File.


Test Endpoints:

Create Customer: POST http://localhost:8080/XBookstoreAPI/api/customers with body:{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "password": "pass123"
}

Response: 201 Created, returns customerId (UUID).
Get Books: GET http://localhost:8080/XBookstoreAPI/api/books.
Add to Cart: POST http://localhost:8080/XBookstoreAPI/api/customers/{customerId}/cart/items with body:{
  "bookId": "5aa4e8a9-6fd3-44fc-afc6-e39aacf6e665",
  "quantity": 1
}


Create Order: POST http://localhost:8080/XBookstoreAPI/api/customers/{customerId}/orders.
Test error cases (e.g., invalid bookId, insufficient stock).



Project Structure
XBookstoreAPI/
├── src/
│   ├── main/
│   │   ├── java/com/mycompany/bookstore/
│   │   │   ├── BookResource.java       # REST endpoints for books
│   │   │   ├── AuthorResource.java     # REST endpoints for authors
│   │   │   ├── CustomerResource.java   # REST endpoints for customers
│   │   │   ├── CartResource.java       # REST endpoints for carts
│   │   │   ├── OrderResource.java      # REST endpoints for orders
│   │   │   ├── InMemoryStorage.java    # In-memory data storage
│   │   │   ├── CORSFilter.java         # Custom CORS filter
│   │   ├── webapp/
│   │   │   ├── WEB-INF/
│   │   │   │   ├── web.xml            # Servlet configuration
│   ├── test/                          # Test resources (empty)
├── pom.xml                            # Maven dependencies and build config
├── XBookstoreAPI.postman_collection.json  # Postman collection

Deployment Notes

CORS: The CORSFilter.java allows requests from http://localhost:3000 (e.g., for a React frontend). If deploying with a frontend on the same server, remove or adjust the filter.
Tomcat Issues: If deployment fails ("context failed to start"):
Check Tomcat/logs/localhost.[date].log for errors (e.g., ClassNotFoundException).
Ensure pom.xml includes all Jersey dependencies (jersey-server, jersey-hk2, jersey-media-json-jackson).
Clear Tomcat cache: Delete Tomcat/work/Catalina/localhost/XBookstoreAPI.
Verify Tomcat 9.x compatibility with Java 8.


Port Conflicts: If port 8080 is in use, change to 8081 in Tomcat/conf/server.xml.

License
This project is licensed under the MIT License - see the LICENSE file for details.

Developed by [Your Name] for SCOSC022W coursework, 2025.
