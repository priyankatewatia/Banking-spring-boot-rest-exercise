# Design

This exercise is implemented as a Microservice using Spring Boot(version 1.5.9) to leverage the Rest APIs and other spring integrations which come along.
Spring Boot is an application bootstrapping framework that makes it easy to create new RESTful services (among other types of applications). 
It provides many of the usual Spring facilities that can be configured easily usually without any XML. 
In addition to easy set up of Spring Controllers, Spring Data etc., Spring Boot comes with the Actuator module that gives the application the endpoints helpful in monitoring and operating the service.

The exercise is having the following project structure:
* Controller: Lists the request mappings for individual bank operations with respective URIs.
* Service: Implements the core business logic hidden with external world.
* Repository: Used for JPA Integration or saving/fetching entities.
* Domain: Includes the model objects/entities used throughout the application.
* Exception: Includes Global exception handler and respective exception classes which are used.


This is a simple Java (version 1.8) application which is using Maven as the build automation tool and demonstrating required Bank operations as:
1. Deposit amount for a customer
2. Withdraw amount for a customer
3. Get Customer's Balance
4. Get Bank's Total Balance

## How to Run 

This application is packaged as a jar which has Tomcat 8 embedded. No Tomcat or JBoss installation is necessary.

* Clone this project and set it up using Intellij IDE.
* Make sure you are using JDK 1.8 and Maven 3.x.
* Right click on the project and select Build Module 'bank-spring-boot-rest' for building it.
* You can use the Run icon in the gutter of the Application.java file next to the class declaration or the main() method declaration to run the application.
* You can run the tests by clicking the Run icon in individual test files.

Once the application runs you should see something like this:

```
2021-03-28 18:06:12.125  INFO 61477 --- [           main] s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat started on port(s): 8090 (http)
2021-03-28 18:06:12.129  INFO 61477 --- [           main] com.exercise.bank.Application            : Started Application in 3.966 seconds (JVM running for 4.331)
```

Access the application at the url "localhost:8090/bank-api/v1" via postman through below operations:
### Deposit Customer's Balance

(Request Body needs below 2 attributes as user_name of String data type and amount of double data type)

```
POST /bank-api/v1/deposit
Accept: application/json
Content-Type: application/json

{
"user_name": "priya",
"amount": 12
}

RESPONSE: HTTP 200 if balance is deposited,
else
HTTP 404 if customer's account was not found and could not be created (ResourceNotFoundException)
```

### Withdraw Customer's Balance

```
POST /bank-api/v1/withdraw
Accept: application/json
Content-Type: application/json

{
"user_name": "priya",
"amount": 12
}

RESPONSE: HTTP 200 if balance is withdrawn,
HTTP 400 if amount to be withdrawn is greater than existing customer's balance,
else
HTTP 404 if customer is not found (ResourceNotFoundException)
```

### Fetch customer's balance

```
GET http://localhost:8090/bank-api/v1/getCustomerBalance/{userName}

RESPONSE: HTTP 200 with balance returned if customer is found 
else
HTTP 404 if customer is not found (ResourceNotFoundException)
```

### Fetch Bank's Total Balance

```
GET http://localhost:8090/bank-api/v1/getTotalBankBalance

RESPONSE: HTTP Status 200 with balance retuned (default as 0.00)
```

Here are some of service info related management endpoints you can call:

### Get information about system health, configurations, etc.

```
http://localhost:8091/env
http://localhost:8091/health
http://localhost:8091/info
http://localhost:8091/metrics
```

## About the Service Assumptions
This is a simple bank exercise REST service to demonstrate the behaviour of the code in a concise manner.
Following assumptions have been made as part of this service:
* A user interface already exists and routes the customer's requests to the hosted backend microservice APIs.
* An HTTP session is created per user and holds session related attributes such as cookies, IAM (Identity Access Management) for authentication specific access token and refresh token etc.
* DB resource and entities already exists with customer's records.

## TradeOffs (Features Not Fully implemented)
* DB Setup for the persistence context via Hibernate ORM.
* Multithreading and Concurrency Support.

## Further Improvements which could have been done with time
* User and admin roles support via Spring Security using OAuth2.
* Swagger API Docs displaying application endpoints via Swagger UI.
* Distributed Tracing and Error Logging.
* Validation for domain objects through javax.validation support.
* Hystrix Fallback Methods and Circuit Breaker integration for spring boot.

