# 📥 QA API Service

The **`qa-api`** service serves as the entry point for incoming HTTP REST API requests in the AI-based question-evaluation system. It acts as a **Producer** in the event-driven architecture — receiving user questions and answers, creating evaluation events, and publishing them to an Apache Kafka topic.

---

## 🏗️ System Role

The service functions as the application gateway:
1. Exposes the public REST endpoint `POST /api/questions/evaluate`.
2. Validates and maps input data into a DTO (`QuestionEvaluationRequest`).
3. Serializes the DTO to JSON and asynchronously publishes it to the Kafka topic: `qa-pending-evaluations`.

---

## 🛠️ Tech Stack

* **Java 17+** / **Spring Boot 3.x**
* **Spring Web** – REST interface handling
* **Spring Kafka** – Event broker integration
* **Jackson Databind** – DTO JSON serialization

---

## ⚙️ Configuration (`application.properties`)

```properties
server.port=8080

# Kafka Connection
spring.kafka.bootstrap-servers=localhost:9092

# Message Serialization
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer