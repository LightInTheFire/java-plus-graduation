# Explore With Me

Социальная сеть для публикации интересных событий и поиска компаний для участия в них.

## Обзор

Explore With Me — микросервисное приложение на Spring Boot, которое позволяет пользователям:
- Публиковать и просматривать события
- Находить компании для участия в событиях
- Отслеживать статистику событий

## Архитектура

Проект состоит из следующих микросервисов:

| Сервис              | Порт | Описание                                 |
|---------------------|------|------------------------------------------|
| Event Service       | 0    | Основной сервис (события, категории)     |
| User Service        | 0    | Сервис пользователей                     |
| Request Service     | 0    | Сервис заявок на участие                 |
| Comment Service     | 0    | Сервис комментариев                      |
| Stats Service       | 0    | Сервис статистики событий                |
| Gateway             | 8080 | API-шлюз                                 |
| Discovery Server    | 8761 | Service Discovery (Eureka)               |
| Config Server       | 0    | Config Server                            |

## Технологический стек

- **Java 21**
- **Spring Boot 3.3.4**
    - Spring Web
    - Spring Data JPA
    - Spring Validation
    - Spring Retry
    - Spring Boot Actuator
- **Spring Cloud 2023.0.3**
    - Spring Cloud Config (Client/Server)
    - Spring Cloud Netflix Eureka
    - Spring Cloud Gateway
- **Maven** — сборка и управление зависимостями
- **Docker & Docker Compose** — контейнеризация
- **PostgreSQL** — основная БД
- **MapStruct** — маппинг DTO
- **Lombok** — уменьшение бойлерплейт кода
- **Jackson** — JSON-сериализация
- **QueryDSL** — типобезопасные запросы
- **Spotless** — форматирование кода

## Модули проекта

- `core/event-service` — Основной сервис (события, категории, подборки)
- `core/user-service` — Сервис пользователей
- `core/request-service` — Сервис заявок на участие в событиях
- `core/comment-service` — Сервис комментариев к событиям
- `core/clients` — Общие Feign-клиенты между сервисами
- `core/api-contracts` — Общие DTO и исключения
- `stats/stats-server` — Сервис статистики
- `stats/stats-dto` — Общие DTO для сервиса статистики
- `stats/stats-client` — Клиент для сервиса статистики
- `infra/gateway-server` — API-шлюз
- `infra/discovery-server` — Service Discovery
- `infra/config-server` — Config Server

## Начало работы

### Требования

- Java 21+
- Maven 3.8+
- Docker & Docker Compose

### Сборка

```bash
mvn clean install -DskipTests
```

### Запуск через Docker Compose

Запустить базы данных PostgreSQL:

```bash
docker-compose up -d
```

### Порты

| Сервис           | Порт |
|------------------|------|
| Event Service    | 0    |
| User Service     | 0    |
| Request Service  | 0    |
| Comment Service  | 0    |
| Stats Service    | 0    |
| Gateway          | 8080 |
| Discovery Server | 8761 |
| Config Server    | 0    |
| PostgreSQL       | 5432 |

## API документация

- [Microservices API](docs/ewm-main-service-spec.json)
- [Stats Service API](docs/ewm-stats-service-spec.json)

Для просмотра используйте:
- [Swagger Editor](https://editor.swagger.io/)

## Тестирование

Коллекции Postman доступны в директории `postman/`.

## Структура проекта

```
java-plus-graduation/
├── core/                    # Основная бизнес-логика
│   ├── event-service/       # Сервис событий
│   ├── user-service/        # Сервис пользователей
│   ├── request-service/     # Сервис заявок
│   ├── comment-service/     # Сервис комментариев
│   ├── clients/             # Общие клиенты
│   └── api-contracts/       # Общие DTO и исключения
├── stats/                   # Cтатистика
│   ├── stats-client/        # Клиент статистики
│   ├── stats-dto/           # DTO статистики
│   └── stats-server/        # Сервер статистики
├── infra/                   # Инфраструктурные компоненты
│   ├── config-server/       # Config Server
│   ├── discovery-server/    # Service Discovery (Eureka)
│   └── gateway-server/      # API Gateway
├── docs/                    # API спецификации (OpenAPI/Swagger)
├── postman/                 # Postman коллекции для тестирования
└── docker-compose.yml       # Конфигурация Docker Compose для инфраструктуры
```
