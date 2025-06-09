# hotel-info-app

REST API приложение для работы с отелями, предоставляющее возможность управления и поиска информации.

## Technologies

Технологии, использованные в проекте:
- Java 17+
- Spring Boot
- Spring Data JPA
- Liquibase
- H2
- Maven
- JUnit/Mockito

## API Endpoints

Основные эндпоинты API для работы с отелями:

| Метод | Эндпоинт | Описание |
|-------|----------|----------|
| **GET** | `/property-view/hotels` | Получение списка всех отелей с их краткой информацией |
| **GET** | `/property-view/hotels/{id}` | Получение расширенной информации по конкретному отелю |
| **POST** | `/property-view/hotels` | Создание нового отеля |
| **POST** | `/property-view/hotels/{id}/amenities` | Добавление списка удобств (amenities) к отелю |
| **GET** | `/property-view/search` | Поиск отелей по параметрам (name, brand, city, county, amenities) |
| **GET** | `/property-view/histogram/{param}` | Получение количества отелей, сгруппированных по параметру (brand, city, county, amenities) |

Полная документация доступна в Swagger UI (`/property-view/swagger-ui/index.html`) после запуска приложения.

## Test Coverage

Тестовое покрытие проекта составляет 94% (данные JaCoCo).