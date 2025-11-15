# Online Cinema - Backend System

Веб-додаток для перегляду фільмів і серіалів онлайн з можливістю підписки.

## Технології

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Spring Security** (JWT authentication)
- **H2 Database** (для розробки) / PostgreSQL (для продакшн)
- **Maven**
- **Lombok**
- **MapStruct** (для маппінгу DTO)
- **SpringDoc OpenAPI** (Swagger)

```
src/main/java/com/onlinecinema/
├── config/              # Конфігурація (Security, OpenAPI)
├── controller/          # REST контролери
├── dto/                 # Data Transfer Objects
├── entity/              # JPA сутності
├── exception/           # Обробка винятків
├── pattern/             # Реалізація патернів проектування
│   ├── factory/
│   ├── observer/
│   └── strategy/
├── repository/          # Spring Data JPA репозиторії
├── security/            # JWT та Security конфігурація
└── service/             # Бізнес-логіка
```

## Основні Функції

### Для Користувачів:
- Реєстрація та автентифікація (JWT)
- Перегляд каталогу фільмів та серіалів
- Пошук та фільтрація (за жанром, роком, країною)
- Підписки (FREE з рекламою / PREMIUM без реклами)
- Додавання до "Вибране"
- Рекомендації на основі історії переглядів
- Історія переглядів

### Для Адміністраторів:
- CRUD операції для фільмів та серіалів
- Управління жанрами
- Управління користувачами
- Генерація звітів:
  - Найпопулярніші фільми/жанри
  - Активність користувачів
  - Доходи від підписок

## API Endpoints

### Публічні:
- `POST /api/auth/register` - реєстрація
- `POST /api/auth/login` - вхід
- `GET /api/movies` - список фільмів
- `GET /api/series` - список серіалів
- `GET /api/genres` - список жанрів

### Для авторизованих користувачів:
- `GET /api/subscriptions/me` - моя підписка
- `POST /api/subscriptions/upgrade` - оновити до преміум
- `GET /api/favorites` - мої обрані
- `GET /api/recommendations/movies` - рекомендації фільмів
- `GET /api/recommendations/series` - рекомендації серіалів

### Для адміністраторів:
- `POST /api/admin/movies` - створити фільм
- `PUT /api/admin/movies/{id}` - оновити фільм
- `DELETE /api/admin/movies/{id}` - видалити фільм
- `GET /api/reports` - звіти

Повна документація API доступна за адресою: `http://localhost:8080/swagger-ui.html`

## Запуск Проекту

### Швидкий Старт

1. **Вимоги**: Java 17+, Maven 3.6+

2. **Запуск додатку**:
```bash
mvn spring-boot:run
```

3. **Доступ до додатку**:
   - API: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - H2 Console: http://localhost:8080/h2-console
     - JDBC URL: `jdbc:h2:mem:cinemadb`
     - Username: `sa`
     - Password: (порожнє)

### Тестові Користувачі

Після запуску автоматично створюються:

- **Адміністратор**: `admin` / `admin123` (PREMIUM)
- **Користувач 1**: `user1` / `user123` (PREMIUM)
- **Користувач 2**: `user2` / `user123` (FREE)

### Тестові Дані

Автоматично створюються:
- **9 жанрів** (Action, Drama, Comedy, Thriller, Sci-Fi, Horror, Romance, Crime, Fantasy)
- **8 фільмів** (The Matrix, Inception, The Shawshank Redemption, The Dark Knight, Pulp Fiction, Forrest Gump, The Godfather, Interstellar)
- **2 серіали** (Breaking Bad, Game of Thrones) з епізодами

Детальні інструкції див. [QUICK_START.md](QUICK_START.md)

## База Даних

### Основні Сутності:
- **User** - користувачі системи
- **Movie** - фільми
- **Series** - серіали
- **Episode** - епізоди серіалів
- **Genre** - жанри
- **Subscription** - підписки користувачів
- **ViewingHistory** - історія переглядів
- **Favorite** - обрані фільми/серіали

## Конфігурація

Файли конфігурації:
- `application.properties` - основна конфігурація
- `application-dev.properties` - для розробки
- `application-prod.properties` - для продакшн

## Тестування

```bash
mvn test
```

