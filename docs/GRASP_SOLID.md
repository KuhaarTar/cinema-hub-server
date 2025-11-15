# GRASP Патерни та SOLID Принципи

## GRASP Патерни (General Responsibility Assignment Software Patterns)

### 1. Information Expert (Інформаційний експерт)

**Принцип:** Призначте відповідальність класу, який має інформацію, необхідну для виконання завдання.

**Реалізація в проекті:**

#### MovieService - експерт по фільмам
**Місцезнаходження:** `src/main/java/com/onlinecinema/service/MovieService.java`

```java
@Service
public class MovieService {
    private final MovieRepository movieRepository;
    
    // MovieService знає, як працювати з фільмами
    public List<MovieDto> findByGenre(Long genreId) {
        return movieRepository.findByGenreId(genreId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
```

#### UserService - експерт по користувачам
**Місцезнаходження:** `src/main/java/com/onlinecinema/service/UserService.java`

```java
@Service
public class UserService {
    private final UserRepository userRepository;
    
    // UserService знає, як працювати з користувачами
    public UserDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToDto(user);
    }
}
```

#### SubscriptionService - експерт по підпискам
**Місцезнаходження:** `src/main/java/com/onlinecinema/service/SubscriptionService.java`

```java
@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionStrategyFactory strategyFactory;
    
    // SubscriptionService знає логіку підписок
    public SubscriptionDto getMySubscription() {
        Long userId = securityUtils.getCurrentUserId();
        Subscription subscription = subscriptionRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        return mapToDto(subscription);
    }
}
```

**Переваги:**
- Кожен сервіс містить логіку для своєї предметної області
- Легше знайти, де знаходиться певна функціональність
- Мінімізація залежностей між компонентами

---

### 2. Creator (Творець)

**Принцип:** Призначте класу B відповідальність за створення об'єкта A, якщо:
- B містить або агрегує A
- B записує A
- B активно використовує A
- B має дані для ініціалізації A

**Реалізація в проекті:**

#### ContentFactory - створює Movie та Series
**Місцезнаходження:** `src/main/java/com/onlinecinema/pattern/factory/ContentFactory.java`

```java
@Component
public class ContentFactory {
    // ContentFactory створює контент, тому що знає, як його ініціалізувати
    public Movie createMovie(String title, String description, Integer releaseYear) {
        return Movie.builder()
                .title(title)
                .description(description)
                .releaseYear(releaseYear)
                .viewCount(0L)
                .build();
    }
}
```

#### Repository - створює та зберігає сутності
**Місцезнаходження:** `src/main/java/com/onlinecinema/repository/*.java`

```java
public interface MovieRepository extends JpaRepository<Movie, Long> {
    // Repository відповідає за створення та збереження сутностей
    Movie save(Movie movie);
}
```

#### Service - створює DTO
**Місцезнаходження:** `src/main/java/com/onlinecinema/service/*.java`

```java
@Service
public class MovieService {
    // Service створює DTO, тому що знає, як мапити Entity в DTO
    private MovieDto mapToDto(Movie movie) {
        return MovieDto.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                // ...
                .build();
    }
}
```

**Переваги:**
- Чітке визначення, хто відповідає за створення об'єктів
- Зменшення зв'язаності між класами

---

### 3. Controller (Контролер)

**Принцип:** Призначте відповідальність за обробку системних подій класу, який представляє всю систему або підсистему.

**Реалізація в проекті:**

#### REST Controllers - координують запити
**Місцезнаходження:** `src/main/java/com/onlinecinema/controller/*.java`

```java
@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;
    private final ViewingHistoryService viewingHistoryService;
    
    // Controller координує запити та делегує роботу сервісам
    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        return ResponseEntity.ok(movieService.findAll());
    }
    
    @PostMapping("/{id}/watch")
    public ResponseEntity<ViewingHistoryDto> watchMovie(@PathVariable Long id) {
        Long userId = securityUtils.getCurrentUserId();
        movieService.incrementViewCount(id);
        ViewingHistoryDto history = viewingHistoryService.recordMovieView(userId, id);
        return ResponseEntity.ok(history);
    }
}
```

**Переваги:**
- Централізована обробка HTTP запитів
- Делегування бізнес-логіки сервісам
- Легке тестування через MockMvc

---

### 4. Low Coupling (Низька зв'язаність)

**Принцип:** Призначте відповідальність так, щоб мінімізувати залежності між класами.

**Реалізація в проекті:**

#### Dependency Injection через інтерфейси
```java
@Service
public class MovieService {
    // Залежність через інтерфейс, а не конкретну реалізацію
    private final MovieRepository movieRepository;
    private final ContentFactory contentFactory;
    
    public MovieService(MovieRepository movieRepository, ContentFactory contentFactory) {
        this.movieRepository = movieRepository;
        this.contentFactory = contentFactory;
    }
}
```

#### Strategy Pattern - слабка зв'язаність
```java
@Service
public class SubscriptionService {
    // Залежність від інтерфейсу, а не конкретних класів
    private final SubscriptionStrategyFactory strategyFactory;
    
    public boolean canWatchWithoutAds(Subscription subscription) {
        SubscriptionStrategy strategy = strategyFactory.getStrategy(subscription.getType());
        return strategy.canWatchWithoutAds();
    }
}
```

**Переваги:**
- Легко замінити реалізацію
- Легше тестувати (можна мокувати)
- Менше впливу змін в одному класі на інші

---

### 5. High Cohesion (Висока згуртованість)

**Принцип:** Призначте відповідальність так, щоб класи мали чітко визначену мету та були зосереджені на одній задачі.

**Реалізація в проекті:**

#### MovieService - тільки логіка фільмів
**Місцезнаходження:** `src/main/java/com/onlinecinema/service/MovieService.java`

```java
@Service
public class MovieService {
    // Всі методи пов'язані з роботою з фільмами
    public List<MovieDto> findAll() { ... }
    public MovieDto findById(Long id) { ... }
    public List<MovieDto> search(String title) { ... }
    public MovieDto create(MovieRequest request) { ... }
    public MovieDto update(Long id, MovieRequest request) { ... }
    public void deleteById(Long id) { ... }
}
```

#### AuthService - тільки автентифікація
**Місцезнаходження:** `src/main/java/com/onlinecinema/service/AuthService.java`

```java
@Service
public class AuthService {
    // Всі методи пов'язані з автентифікацією
    public AuthResponseDto login(LoginRequest request) { ... }
    // Не містить логіки роботи з фільмами або підписками
}
```

**Переваги:**
- Легше зрозуміти призначення класу
- Легше підтримувати та тестувати
- Менше ризику побічних ефектів

---

## SOLID Принципи

### 1. Single Responsibility Principle (SRP) - Принцип єдиної відповідальності

**Принцип:** Клас повинен мати тільки одну причину для зміни.

**Реалізація в проекті:**

#### MovieService - тільки логіка фільмів
```java
@Service
public class MovieService {
    // Відповідає тільки за роботу з фільмами
    // Якщо зміниться логіка фільмів - змінюється тільки цей клас
}
```

#### SubscriptionService - тільки логіка підписок
```java
@Service
public class SubscriptionService {
    // Відповідає тільки за роботу з підписками
    // Якщо зміниться логіка підписок - змінюється тільки цей клас
}
```

#### AuthService - тільки автентифікація
```java
@Service
public class AuthService {
    // Відповідає тільки за автентифікацію
    // Якщо зміниться логіка автентифікації - змінюється тільки цей клас
}
```

**Переваги:**
- Легше зрозуміти код
- Легше тестувати
- Менше ризику побічних ефектів при змінах

---

### 2. Open/Closed Principle (OCP) - Принцип відкритості/закритості

**Принцип:** Програмні сутності повинні бути відкриті для розширення, але закриті для модифікації.

**Реалізація в проекті:**

#### Strategy Pattern - додавання нових типів підписок
**Місцезнаходження:** `src/main/java/com/onlinecinema/pattern/strategy/`

```java
// Можна додати новий тип підписки без зміни існуючого коду
public class StudentSubscriptionStrategy implements SubscriptionStrategy {
    @Override
    public boolean canWatchWithoutAds() {
        return false;
    }
    
    @Override
    public double getPrice() {
        return 4.99; // Знижка для студентів
    }
}

// Додати в Factory
public SubscriptionStrategy getStrategy(SubscriptionType type) {
    return switch (type) {
        case FREE -> new FreeSubscriptionStrategy();
        case PREMIUM -> new PremiumSubscriptionStrategy();
        case STUDENT -> new StudentSubscriptionStrategy(); // Новий тип
    };
}
```

#### Observer Pattern - додавання нових спостерігачів
**Місцезнаходження:** `src/main/java/com/onlinecinema/pattern/observer/`

```java
// Можна додати нового спостерігача без зміни існуючого коду
@Component
public class RatingBasedRecommendationObserver implements RecommendationObserver {
    @Override
    public void update(User user, ViewingHistory viewingHistory) {
        // Нова логіка рекомендацій на основі рейтингів
    }
}
```

**Переваги:**
- Легко розширювати функціональність
- Менше ризику зламати існуючий код
- Дотримання принципу "не чіпай, що працює"

---

### 3. Liskov Substitution Principle (LSP) - Принцип підстановки Лісков

**Принцип:** Об'єкти підкласів повинні бути здатні замінювати об'єкти базових класів без зміни коректності програми.

**Реалізація в проекті:**

#### Strategy Pattern - стратегії взаємозамінні
```java
// Будь-яка стратегія може замінити інтерфейс SubscriptionStrategy
SubscriptionStrategy freeStrategy = new FreeSubscriptionStrategy();
SubscriptionStrategy premiumStrategy = new PremiumSubscriptionStrategy();

// Обидві працюють однаково в коді
boolean canWatch1 = freeStrategy.canWatchWithoutAds();
boolean canWatch2 = premiumStrategy.canWatchWithoutAds();
```

#### Observer Pattern - спостерігачі взаємозамінні
```java
// Будь-який спостерігач може замінити інтерфейс RecommendationObserver
RecommendationObserver genreObserver = new GenreBasedRecommendationObserver();
RecommendationObserver ratingObserver = new RatingBasedRecommendationObserver();

// Обидва працюють однаково
recommendationSubject.attach(genreObserver);
recommendationSubject.attach(ratingObserver);
```

**Переваги:**
- Гнучкість у використанні
- Легке тестування (можна підміняти реалізації)
- Дотримання контрактів інтерфейсів

---

### 4. Interface Segregation Principle (ISP) - Принцип розділення інтерфейсів

**Принцип:** Клієнти не повинні залежати від інтерфейсів, які вони не використовують.

**Реалізація в проекті:**

#### Розділені інтерфейси за функціональністю
```java
// SubscriptionStrategy - тільки методи для підписок
public interface SubscriptionStrategy {
    boolean canWatchWithoutAds();
    double getPrice();
}

// RecommendationObserver - тільки методи для спостерігачів
public interface RecommendationObserver {
    void update(User user, ViewingHistory viewingHistory);
}

// Кожен інтерфейс має чітку мету
// Класи не змушені реалізовувати методи, які їм не потрібні
```

**Переваги:**
- Менше залежностей
- Легше реалізовувати інтерфейси
- Чіткіше призначення інтерфейсів

---

### 5. Dependency Inversion Principle (DIP) - Принцип інверсії залежностей

**Принцип:** Залежності повинні бути на абстракціях, а не на конкретних реалізаціях.

**Реалізація в проекті:**

#### Залежності через інтерфейси
```java
@Service
public class MovieService {
    // Залежність від інтерфейсу Repository, а не конкретної реалізації
    private final MovieRepository movieRepository;
    
    // Залежність від інтерфейсу Factory
    private final ContentFactory contentFactory;
    
    public MovieService(MovieRepository movieRepository, ContentFactory contentFactory) {
        this.movieRepository = movieRepository;
        this.contentFactory = contentFactory;
    }
}
```

#### Strategy Pattern - залежність від інтерфейсу
```java
@Service
public class SubscriptionService {
    // Залежність від інтерфейсу, а не конкретних класів
    private final SubscriptionStrategyFactory strategyFactory;
    
    public boolean canWatchWithoutAds(Subscription subscription) {
        // Повертає інтерфейс, а не конкретний клас
        SubscriptionStrategy strategy = strategyFactory.getStrategy(subscription.getType());
        return strategy.canWatchWithoutAds();
    }
}
```

**Переваги:**
- Легко замінити реалізацію
- Легше тестувати (можна мокувати)
- Менша зв'язаність між компонентами

---

## Взаємодія GRASP та SOLID

### Приклад: MovieService

```java
@Service  // Singleton через Spring
public class MovieService {
    // Low Coupling - залежності через інтерфейси (DIP)
    private final MovieRepository movieRepository;  // Repository Pattern
    private final ContentFactory contentFactory;    // Factory Pattern
    
    // High Cohesion - всі методи пов'язані з фільмами (SRP)
    public List<MovieDto> findAll() { ... }
    public MovieDto findById(Long id) { ... }
    
    // Information Expert - знає, як працювати з фільмами
    private MovieDto mapToDto(Movie movie) { ... }
}
```

**Застосовані принципи:**
- **GRASP Information Expert** - MovieService знає про фільми
- **GRASP Low Coupling** - залежності через інтерфейси
- **GRASP High Cohesion** - всі методи про фільми
- **SOLID SRP** - тільки логіка фільмів
- **SOLID DIP** - залежності від абстракцій

---

## Висновок

Проект дотримується всіх 5 GRASP патернів та всіх 5 SOLID принципів:

**GRASP:**
1. ✅ Information Expert
2. ✅ Creator
3. ✅ Controller
4. ✅ Low Coupling
5. ✅ High Cohesion

**SOLID:**
1. ✅ Single Responsibility Principle
2. ✅ Open/Closed Principle
3. ✅ Liskov Substitution Principle
4. ✅ Interface Segregation Principle
5. ✅ Dependency Inversion Principle

Це забезпечує високу якість коду, легкість підтримки та розширення системи.

