
# 🚀 Team-Based Request Management API

The **Team-Based Request Management API** distributes incoming requests among agents from different teams, ensuring each agent can handle multiple requests simultaneously. When an agent reaches their limit, new requests are added to a queue and automatically assigned to the next available agent.

Built using **Java 21** and **Spring WebFlux**, this API follows **Clean Architecture** and **SOLID** principles, and implements design patterns such as **Command** and **Strategy**.

Reactive programming is used to enhance performance and scalability, enabling non-blocking data flows and optimal resource utilization.

The API is integrated with **CI** via **[GitHub Actions](https://github.com/dowglasmaia/auto-atendimento-service-api/actions)** and uses **SonarQube** for continuous code quality analysis.

You can access the [API contract](src/main/resources/openapi/REQUEST-API.yaml), developed with **Swagger/OpenAPI**, for detailed endpoint and technical specifications.

You can also access the [Postman collection](src/main/resources/collection_postman/auto-atendimento-service-api.postman_collection.json), created to facilitate manual testing of the API.

---

## ⚡ Technologies

These are some of the technologies and tools used in the project:

![SonarCloud Coverage](https://sonarcloud.io/api/project_badges/measure?project=dowglasmaia_auto-atendimento-service-api\&metric=coverage)
![SonarQube](https://img.shields.io/badge/-SonarQube-4E9BCD?style=flat-square\&logo=sonarqube\&logoColor=white)
![Java 21](https://img.shields.io/badge/-Java%2021-007396?style=flat-square\&logo=java\&logoColor=white)
![Spring WebFlux](https://img.shields.io/badge/-Spring%20WebFlux-6DB33F?style=flat-square\&logo=spring\&logoColor=white)
![Docker](https://img.shields.io/badge/-Docker-2496ED?style=flat-square\&logo=docker\&logoColor=white)
![JUnit](https://img.shields.io/badge/-JUnit-25A162?style=flat-square\&logo=junit5\&logoColor=white)
![Mockito](https://img.shields.io/badge/-Mockito-25A162?style=flat-square\&logo=mockito\&logoColor=white)
![Rest-Assured](https://img.shields.io/badge/-Rest--Assured-008CBA?style=flat-square\&logo=rest-assured\&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/-GitHub%20Actions-2088FF?style=flat-square\&logo=github-actions\&logoColor=white)

---

### 🧭 High-Level Architecture Overview

```
┌────────────────────────────────────────────────────────────┐
│                        API Gateway                         │
│                   (e.g., Spring WebFlux)                   │
└────────────────────────────────────────────────────────────┘
                             │
                             ▼
┌────────────────────────────────────────────────────────────┐
│                    Application Layer                       │
│  - Controllers: Handle HTTP requests and responses         │
│  - Services: Coordinate use cases and business logic       │
└────────────────────────────────────────────────────────────┘
                             │
                             ▼
┌────────────────────────────────────────────────────────────┐
│                       Domain Layer                         │
│  - Models: Define core entities (e.g., Request, Agent)     │
│  - Enums: Define request types (CARD_ISSUE, LOAN_REQUEST)  │
│  - Commands: Encapsulate business operations               │
└────────────────────────────────────────────────────────────┘
                             │
                             ▼
┌────────────────────────────────────────────────────────────┐
│                    Infrastructure Layer                    │
│  - Repositories: Data access implementations               │
│  - Configurations: Application settings and beans          │
│  - Logging: Logging configurations and utilities           │
└────────────────────────────────────────────────────────────┘
                             │
                             ▼
┌────────────────────────────────────────────────────────────┐
│                          Database                          │
│        (e.g., Reactive Database via R2DBC or MongoDB)      │
└────────────────────────────────────────────────────────────┘
```

---

### 🔄 Request Flow

1. **Client Interaction**: A client sends an HTTP request to the API Gateway.
2. **Controller Handling**: The request is routed to the appropriate controller in the Application Layer.
3. **Service Coordination**: The controller delegates processing to a service, which coordinates necessary operations.
4. **Domain Logic Execution**: The service invokes domain commands to execute business rules.
5. **Data Persistence**: The domain logic interacts with repositories in the Infrastructure Layer to persist or retrieve data.
6. **Response Generation**: The result is propagated back up through the layers to the controller, which sends an HTTP response to the client.

---


## 📑 Features

* **Automatic Request Distribution**: The API automatically distributes requests to available agents or queues them if all are busy.
* **Request Queue**: Requests are queued by team and type. When all agents are busy, requests are added to the queue.
* **Request Completion**: Agents can complete requests via API, freeing up their slot for new ones.
* **Request Types**:

  * **CARD\_ISSUE**: Issues related to cards.
  * **LOAN\_REQUEST**: Loan requests.
  * **OTHER**: Other request types.

---

## 📦 Project Structure

```plaintext
src
├── main
│   ├── java
│   │   └── com.dowglasmaia.autoatendimentoserviceapi
│   │       ├── application
│   │       │   ├── controller        
│   │       │   └── service           
│   │       ├── domain
│   │       │   ├── model             
│   │       │   ├── enums           
│   │       │   └── command           
│   │       └── infrastructure
│   │           ├── config            
│   │           └── logging           
│   │           └── repository        
│   └── resources
│       └── application.yml
│       └── openapi/REQUEST-API.yaml  <!-- API contract -->        
└── test
    └── java
        └── com.dowglasmaia.autoatendimentoserviceapi
            ├── application.controller 
            └── domain                 
```

---

## 📚 Documentation

### Endpoints

#### 1. Create a new request

* **Endpoint**: `POST /api/requests`
* **Description**: Creates a new request and either assigns it to an available agent or queues it.

**Request Body**:

```json
{
  "id": "1",
  "type": "CARD_ISSUE",
  "customerId": "123"
}
```

**Example**:

```bash
curl -X POST http://localhost:8080/api/requests \
     -H "Content-Type: application/json" \
     -d '{"id": "1", "type": "CARD_ISSUE", "customerId": "123"}'
```

#### 2. Complete a request

* **Endpoint**: `PUT /api/requests/{agentId}/complete`
* **Description**: An agent completes a request. If there are pending requests in the queue, the next one is automatically assigned.

**Parameters**:

* `agentId` (Path Variable): The ID of the agent completing the request.
* `requestId` (Query Parameter): The ID of the request to be completed.
* `requestType` (Query Parameter): The request type.

**Example**:

```bash
curl -X PUT "http://localhost:8080/api/requests/agent1/complete?requestId=1&requestType=CARD_ISSUE"
```

**Responses**:

* **200 OK**: Request completed successfully and next request assigned (if available).
* **404 Not Found**: Agent or request not found.
* **500 Internal Server Error**: An error occurred during request processing.

---

## ✅ Test Scenarios

Integration and unit tests ensure the correct behavior of the API's main flows:

### Integration Tests (`RequestControllerIntegrationTest`)

* **Loan Request Creation** (`shouldHandleLoanRequest`)
* **Loan Request Completion** (`shouldCompleteLoanRequest`)
* **Card Issue Request Creation** (`shouldHandleCardIssueRequest`)
* **Card Issue Request Completion** (`shouldCompleteCardIssueRequest`)
* **Other Requests Creation** (`shouldHandleOtherRequest`)
* **Other Requests Completion** (`shouldCompleteOtherRequest`)

### Unit Tests (Command Pattern)

* **Class: `CardRequestCommandTest`**

  * Assign request to available agent
  * Queue when agents are busy
  * Dequeue next request on completion

* **Class: `LoanRequestCommandTest`** — similar tests for loan flow.

* **Class: `OtherRequestCommandTest`** — similar tests for "Other" requests.

* **Class: `CommandInvokerTest`**

  * Execute multiple commands in sequence.

---

## 🔌 Design Patterns

### Command Pattern

Encapsulates requests as objects to standardize processing and queueing.

* **Commands**: `CardRequestCommand`, `LoanRequestCommand`, `OtherRequestCommand`
* **Executor**: `CommandInvoker`

### Strategy Pattern

Defines strategies for distributing requests across agents, switching dynamically by request type.

* **Context**: `RequestDistributionService`
* **Strategies**: Custom strategies for **Card Issues**, **Loan Requests**, and **Other** types.

---

### ⚙️ Technologies & Tools

* **Java 21**: Core programming language.
* **Spring WebFlux**: Reactive web framework for handling asynchronous requests.
* **Docker**: Containerization for deployment consistency.
* **JUnit & Mockito**: Testing frameworks for unit and integration tests.
* **Rest-Assured**: Simplifies testing of REST services.
* **SonarQube**: Continuous code quality analysis.
* **GitHub Actions**: CI/CD pipeline automation.

---

## 🚀 Execution & Configuration

### Prerequisites

* **Java 21** installed.
* **Docker** installed (optional, for containerized execution).

### Running the App

1. **Clone the repository**:

   ```bash
   git clone https://github.com/dowglasmaia/auto-atendimento-service-api
   ```

2. **Run with Docker**:

   ```bash
   docker-compose up --build
   ```

App will be available at: `http://localhost:8099`

---

## 🌐 References

* [Spring WebFlux Documentation](https://docs.spring.io/spring-framework/reference/web/webflux.html)
* [Java 21 Documentation](https://docs.oracle.com/en/java/)
* [Command Pattern](https://refactoring.guru/design-patterns/command)
* [Strategy Pattern](https://refactoring.guru/design-patterns/strategy)
* [Rest-Assured Documentation](https://rest-assured.io/)
* [Swagger OpenAPI](https://swagger.io/specification/)
