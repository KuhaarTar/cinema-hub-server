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

## Архітектура та Патерни Проектування

### Документація

📚 **Повна документація доступна в папці `docs/`:**
- [Use Cases Діаграма](docs/USE_CASES.md) - детальний опис всіх use cases системи
- [UML-діаграма класів](docs/UML_CLASS_DIAGRAM.md) - повна структура класів системи
- [Детальна документація GoF-патернів](docs/DESIGN_PATTERNS_DETAILED.md) - опис всіх патернів з місцезнаходженням
- [GRASP та SOLID](docs/GRASP_SOLID.md) - застосування архітектурних принципів

### GoF Патерни:

1. **Strategy Pattern** - для різних типів підписок (FREE/PREMIUM)
   - **Місцезнаходження:** `src/main/java/com/onlinecinema/pattern/strategy/`
   - `SubscriptionStrategy` - інтерфейс стратегії
   - `FreeSubscriptionStrategy` - безкоштовна підписка з рекламою
   - `PremiumSubscriptionStrategy` - преміум підписка без реклами
   - `SubscriptionStrategyFactory` - фабрика для створення стратегій

2. **Factory Pattern** - для створення контенту (фільми/серіали)
   - **Місцезнаходження:** `src/main/java/com/onlinecinema/pattern/factory/ContentFactory.java`
   - `ContentFactory` - фабрика для створення Movie та Series об'єктів

3. **Observer Pattern** - для системи рекомендацій
   - **Місцезнаходження:** `src/main/java/com/onlinecinema/pattern/observer/`
   - `RecommendationObserver` - інтерфейс спостерігача
   - `RecommendationSubject` - суб'єкт, що сповіщає спостерігачів
   - `GenreBasedRecommendationObserver` - конкретний спостерігач для рекомендацій на основі жанрів

4. **Repository Pattern** - реалізований через Spring Data JPA
   - **Місцезнаходження:** `src/main/java/com/onlinecinema/repository/*.java`
   - Всі репозиторії наслідуються від `JpaRepository`

5. **Builder Pattern** - використовується через Lombok `@Builder`
   - **Місцезнаходження:** Всі Entity та DTO класи
   - Використовується для створення складних об'єктів

6. **Singleton Pattern** - через Spring `@Component`, `@Service`, `@Repository`
   - **Місцезнаходження:** Всі сервіси та компоненти
   - Spring автоматично створює один екземпляр

7. **Adapter Pattern** - для інтеграції з Spring Security
   - **Місцезнаходження:** `src/main/java/com/onlinecinema/security/CustomUserDetailsService.java`
   - Адаптує User entity до Spring Security UserDetails

8. **Facade Pattern** - через REST контролери
   - **Місцезнаходження:** `src/main/java/com/onlinecinema/controller/*.java`
   - Надає спрощений API інтерфейс до складної бізнес-логіки

### GRASP Патерни:

- **Information Expert** - кожен сервіс відповідає за свою область
  - `MovieService` - експерт по фільмам
  - `UserService` - експерт по користувачам
  - `SubscriptionService` - експерт по підпискам

- **Creator** - репозиторії створюють сутності, Factory створює об'єкти
  - `ContentFactory` - створює Movie та Series
  - `MovieRepository` - зберігає Movie

- **Controller** - контролери координують запити
  - `MovieController`, `AuthController`, `AdminController`

- **Low Coupling** - мінімальна залежність між компонентами через інтерфейси
  - Dependency Injection через конструктори
  - Залежності від абстракцій, а не конкретних класів

- **High Cohesion** - кожен клас має чітку відповідальність
  - `MovieService` - тільки логіка фільмів
  - `AuthService` - тільки автентифікація

### SOLID Принципи:

- **Single Responsibility** - кожен клас має одну відповідальність
  - `MovieService` - тільки логіка фільмів
  - `SubscriptionService` - тільки логіка підписок

- **Open/Closed** - Strategy pattern дозволяє додавати нові типи підписок без зміни існуючого коду
  - Можна додати `StudentSubscriptionStrategy` без зміни `SubscriptionService`

- **Liskov Substitution** - стратегії можуть замінювати одна одну
  - `FreeSubscriptionStrategy` та `PremiumSubscriptionStrategy` взаємозамінні

- **Interface Segregation** - інтерфейси розділені за функціональністю
  - `SubscriptionStrategy` - тільки методи для підписок
  - `RecommendationObserver` - тільки методи для спостерігачів

- **Dependency Inversion** - залежності через інтерфейси
  - Сервіси залежать від Repository інтерфейсів, а не конкретних реалізацій

## Структура Проекту

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

## Автор

Розроблено для курсової роботи з проектування програмного забезпечення.

