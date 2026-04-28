# Explore With Me

Социальная сеть для публикации интересных событий и поиска компаний для участия в них.

## Обзор

Explore With Me — микросервисное приложение на Spring Boot, которое позволяет пользователям:
- Публиковать и просматривать события
- Находить компании для участия в событиях
- Отслеживать статистику событий

## Архитектура

Проект состоит из следующих микросервисов:

| Сервис        | Порт | Описание                   |
|---------------|------|----------------------------|
| Main Service  | 8080 | Основная бизнес-логика     |
| Stats Service | 9090 | Статистика событий         |
| Gateway       | 8085 | API-шлюз                   |
| Discovery     | 8761 | Service Discovery (Eureka) |
| Config        | 8888 | Config Server              |

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
- **Spotless** — форматирование кода

## Модули проекта

- `core/main-service` — Основной сервис (события, пользователи, категории)
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

| Сервис           | Порт  |
|------------------|-------|
| Main Service     | 8080  |
| Stats Service    | 9090  |
| Gateway          | 8085  |
| Discovery Server | 8761  |
| Config Server    | 8888  |
| Stats DB         | 13000 |
| Main DB          | 13001 |

## API документация

- [Main Service API](docs/ewm-main-service-spec.json)
- [Stats Service API](docs/ewm-stats-service-spec.json)

Для просмотра используйте:
- [Swagger Editor](https://editor.swagger.io/)

## Тестирование

Коллекции Postman доступны в директории `postman/`.

## Структура проекта

```
java-plus-graduation/
├── core/                    # Модуль основного сервиса
│   └── main-service/
├── stats/                   # Модуль сервиса статистики
│   ├── stats-client/
│   ├── stats-dto/
│   └── stats-server/
├── infra/                   # Инфраструктурные модули
│   ├── config-server/
│   ├── discovery-server/
│   └── gateway-server/
├── docs/                    # API спецификации
├── postman/                 # Postman тесты
└── docker-compose.yml
```
