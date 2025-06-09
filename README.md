# hotel-info-app

REST API ���������� ��� ������ � �������, ��������������� ����������� ���������� � ������ ����������.

## Technologies

����������, �������������� � �������:
- Java 17+
- Spring Boot
- Spring Data JPA
- Liquibase
- H2
- Maven
- JUnit/Mockito

## API Endpoints

�������� ��������� API ��� ������ � �������:

| ����� | �������� | �������� |
|-------|----------|----------|
| **GET** | `/property-view/hotels` | ��������� ������ ���� ������ � �� ������� ����������� |
| **GET** | `/property-view/hotels/{id}` | ��������� ����������� ���������� �� ����������� ����� |
| **POST** | `/property-view/hotels` | �������� ������ ����� |
| **POST** | `/property-view/hotels/{id}/amenities` | ���������� ������ ������� (amenities) � ����� |
| **GET** | `/property-view/search` | ����� ������ �� ���������� (name, brand, city, county, amenities) |
| **GET** | `/property-view/histogram/{param}` | ��������� ���������� ������, ��������������� �� ��������� (brand, city, county, amenities) |

������ ������������ �������� � Swagger UI (`/property-view/swagger-ui/index.html`) ����� ������� ����������.

## Test Coverage

�������� �������� ������� ���������� 94% (������ JaCoCo).