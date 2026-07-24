# 🤖 Event-Driven QA Evaluation System

An asynchronous, event-driven microservices system built with Spring Boot and Apache Kafka that automatically evaluates user answers to questions using Large Language Models (LLM / Groq API).

---

## 🏗️ Architecture Overview

The system is decoupled into two independent microservices communicating asynchronously via Apache Kafka to offload long-running AI evaluations from the primary API:

1. **`qa-api`** (`Port 8080`): Gateway service exposing the public REST API. Accepts evaluation requests, validates them, and produces event messages to a Kafka topic.
2. **`qa-evaluator`** (`Port 8081`): Consumer service listening to Kafka events. Integrates with **Spring AI** and the **Groq API (Llama 3.1)** to perform automated, context-aware evaluations of user answers.

---

## 🛠️ Tech Stack & Prerequisites

* **Java 17+** / **Spring Boot 3.x**
* **Apache Kafka** – Asynchronous event broker
* **Spring AI** – Framework for LLM integration (Groq / Llama 3.1)
* **Docker & Docker Compose** – Containerization for Kafka, ZooKeeper, and AKHQ
* **AKHQ** – Web UI for Apache Kafka management
* **Jackson Databind** – JSON serialization and deserialization

---

## ⚙️ Project Structure & Configuration

* **`qa-api/`** — Spring Boot Producer API
* **`qa-evaluator/`** — Spring Boot Consumer & AI Evaluator
* **`docker-compose.yml`** — Kafka, ZooKeeper, and AKHQ infrastructure setup
* **`README.md`** — Global documentation

### Infrastructure (`docker-compose.yml`)

The provided Docker Compose setup provisions Kafka, ZooKeeper, and AKHQ:

* **Kafka Broker**: `localhost:9092`
* **ZooKeeper**: `localhost:2181`
* **AKHQ Dashboard**: `http://localhost:8080` (or `8082` depending on your mapping)

### Environment Setup

Before starting `qa-evaluator`, you must obtain an API Key from [Groq Cloud](https://console.groq.com/) and expose it via an environment variable.

#### Linux / macOS:
```bash
export SPRING_AI_OPENAI_API_KEY=gsk_YourGroqApiKeyHere
```
Windows (PowerShell):
```PowerShell
$env:SPRING_AI_OPENAI_API_KEY="gsk_YourGroqApiKeyHere"
```
### Application Properties
qa-evaluator uses the OpenAI-compatible base URL for Groq in src/main/resources/application.properties:

### Properties
server.port=8081

## KAFKA CONFIGURATION
```properties
# KAFKA CONFIGURATION
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=qa-evaluator-group
spring.kafka.consumer.auto-offset-reset=earliest

# SPRING AI / GROQ CONFIGURATION
spring.ai.openai.api-key=${SPRING_AI_OPENAI_API_KEY}
spring.ai.openai.base-url=[https://api.groq.com/openai/v1](https://api.groq.com/openai/v1)
spring.ai.openai.chat.options.model=llama-3.1-8b-instant
🚀 Getting Started
1. Start Infrastructure
   Launch Kafka, ZooKeeper, and AKHQ containers:
```
## Getting Started
```Bash
docker-compose up -d
```
### Run Services
   You can run both microservices concurrently via Maven or your preferred IDE.

### Run qa-api (Producer)
```Bash
cd qa-api
./mvnw spring-boot:run
```
### Run qa-evaluator (Consumer)
```Bash
cd qa-evaluator
./mvnw spring-boot:run
```
## 🧪 Usage & Testing
To trigger an evaluation, send an HTTP POST request to qa-api:

URL: http://localhost:8080/api/questions/evaluate

Header: Content-Type: application/json

### Request Body Example:
```JSON
{
"question": "What is the difference between an abstract class and an interface in Java?",
"userAnswer": "An abstract class can hold state and constructors, whereas an interface primarily defines a contract for methods."
}
```
### Processing Flow:
1. qa-api responds immediately with an acknowledgment status.

2. The request payload is published to Kafka topic qa-pending-evaluations.
3.  qa-evaluator consumes the message, constructs a prompt, communicates with Groq (Llama 3.1), and logs/processes the structured evaluation results.