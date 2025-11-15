# Детальна документація GoF-патернів проектування

## 1. Factory Pattern (Фабрика)

### Призначення
Інкапсулює процес створення об'єктів, дозволяючи делегувати створення підкласам або окремим класам.

### Реалізація в проекті

#### 1.1 ContentFactory
**Місцезнаходження:** `src/main/java/com/onlinecinema/pattern/factory/ContentFactory.java`

**Призначення:** Створення об'єктів Movie та Series з валідацією та ініціалізацією значень за замовчуванням.

**Код:**
```java
@Component
public class ContentFactory {
    public Movie createMovie(String title, String description, Integer releaseYear) {
        return Movie.builder()
                .title(title)
                .description(description)
                .releaseYear(releaseYear)
                .viewCount(0L)
                .build();
    }
    
    public Series createSeries(String title, String description, Integer releaseYear) {
        return Series.builder()
                .title(title)
                .description(description)
                .releaseYear(releaseYear)
                .viewCount(0L)
                .build();
    }
}
```

**Використання:**
- `MovieService.create()` - створення нових фільмів
- `SeriesService.create()` - створення нових серіалів

**Переваги:**
- Централізована логіка створення
- Легко змінити процес створення в одному місці
- Інкапсуляція складності створення об'єктів

#### 1.2 SubscriptionStrategyFactory
**Місцезнаходження:** `src/main/java/com/onlinecinema/pattern/strategy/SubscriptionStrategyFactory.java`

**Призначення:** Створення стратегій підписок залежно від типу.

**Код:**
```java
@Component
public class SubscriptionStrategyFactory {
    public SubscriptionStrategy getStrategy(Subscription.SubscriptionType type) {
        return switch (type) {
            case FREE -> new FreeSubscriptionStrategy();
            case PREMIUM -> new PremiumSubscriptionStrategy();
        };
    }
}
```

**Використання:**
- `SubscriptionService` - отримання стратегії для перевірки прав доступу

---

## 2. Strategy Pattern (Стратегія)

### Призначення
Визначає сімейство алгоритмів, інкапсулює кожен з них і робить їх взаємозамінними. Стратегія дозволяє змінювати алгоритм незалежно від клієнтів, які його використовують.

### Реалізація в проекті

**Місцезнаходження:**
- `src/main/java/com/onlinecinema/pattern/strategy/SubscriptionStrategy.java` (інтерфейс)
- `src/main/java/com/onlinecinema/pattern/strategy/FreeSubscriptionStrategy.java`
- `src/main/java/com/onlinecinema/pattern/strategy/PremiumSubscriptionStrategy.java`
- `src/main/java/com/onlinecinema/pattern/strategy/SubscriptionStrategyFactory.java`

**Призначення:** Інкапсуляція логіки різних типів підписок (FREE з рекламою, PREMIUM без реклами).

**Структура:**
```java
// Інтерфейс стратегії
public interface SubscriptionStrategy {
    boolean canWatchWithoutAds();
    double getPrice();
}

// Конкретні стратегії
public class FreeSubscriptionStrategy implements SubscriptionStrategy {
    @Override
    public boolean canWatchWithoutAds() {
        return false; // Free користувачі бачать рекламу
    }
    
    @Override
    public double getPrice() {
        return 0.0;
    }
}

public class PremiumSubscriptionStrategy implements SubscriptionStrategy {
    @Override
    public boolean canWatchWithoutAds() {
        return true; // Premium користувачі не бачать рекламу
    }
    
    @Override
    public double getPrice() {
        return 9.99;
    }
}
```

**Використання:**
```java
// У SubscriptionService
SubscriptionStrategy strategy = strategyFactory.getStrategy(subscription.getType());
boolean canWatchWithoutAds = strategy.canWatchWithoutAds();
```

**Переваги:**
- Легко додати нові типи підписок (наприклад, FAMILY, STUDENT)
- Відкрито для розширення, закрито для модифікації (Open/Closed Principle)
- Кожна стратегія інкапсулює свою логіку
- Дотримання Single Responsibility Principle

---

## 3. Observer Pattern (Спостерігач)

### Призначення
Визначає залежність "один-до-багатьох" між об'єктами так, що коли один об'єкт змінює свій стан, всі залежні об'єкти автоматично отримують сповіщення.

### Реалізація в проекті

**Місцезнаходження:**
- `src/main/java/com/onlinecinema/pattern/observer/RecommendationObserver.java` (інтерфейс)
- `src/main/java/com/onlinecinema/pattern/observer/RecommendationSubject.java` (суб'єкт)
- `src/main/java/com/onlinecinema/pattern/observer/GenreBasedRecommendationObserver.java` (конкретний спостерігач)

**Призначення:** Система рекомендацій, яка реагує на події перегляду контенту користувачами.

**Структура:**
```java
// Інтерфейс спостерігача
public interface RecommendationObserver {
    void update(User user, ViewingHistory viewingHistory);
}

// Суб'єкт
@Component
public class RecommendationSubject {
    private final List<RecommendationObserver> observers = new ArrayList<>();
    
    public void attach(RecommendationObserver observer) {
        observers.add(observer);
    }
    
    public void detach(RecommendationObserver observer) {
        observers.remove(observer);
    }
    
    public void notifyObservers(User user, ViewingHistory viewingHistory) {
        observers.forEach(observer -> observer.update(user, viewingHistory));
    }
}

// Конкретний спостерігач
@Component
public class GenreBasedRecommendationObserver implements RecommendationObserver {
    @Override
    public void update(User user, ViewingHistory viewingHistory) {
        // Логіка оновлення рекомендацій на основі жанрів
    }
}
```

**Використання:**
```java
// У ViewingHistoryService після запису перегляду
recommendationSubject.notifyObservers(user, viewingHistory);
```

**Переваги:**
- Слабка зв'язаність між компонентами
- Легко додати нових спостерігачів (наприклад, RatingBasedRecommendationObserver)
- Динамічна підписка/відписка спостерігачів
- Дотримання Open/Closed Principle

---

## 4. Repository Pattern (Репозиторій)

### Призначення
Інкапсулює логіку доступу до даних, надаючи більш об'єктно-орієнтоване представлення персистентного шару.

### Реалізація в проекті

**Місцезнаходження:** `src/main/java/com/onlinecinema/repository/*.java`

**Призначення:** Абстракція доступу до бази даних через Spring Data JPA.

**Приклади:**
- `MovieRepository extends JpaRepository<Movie, Long>`
- `UserRepository extends JpaRepository<User, Long>`
- `SubscriptionRepository extends JpaRepository<Subscription, Long>`

**Використання:**
```java
@Service
public class MovieService {
    private final MovieRepository movieRepository;
    
    public List<MovieDto> findAll() {
        return movieRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
```

**Переваги:**
- Абстракція доступу до даних
- Легке тестування (можна мокувати)
- Стандартизований підхід до роботи з БД
- Дотримання Dependency Inversion Principle

---

## 5. Builder Pattern (Будівельник)

### Призначення
Відокремлює конструювання складного об'єкта від його представлення, так що в результаті одного і того ж процесу конструювання можуть виходити різні представлення.

### Реалізація в проекті

**Місцезнаходження:** Всі Entity класи з анотацією `@Builder` від Lombok

**Призначення:** Створення складних об'єктів з багатьма опціональними полями.

**Приклади:**
```java
// Entity з Builder
@Entity
@Builder
public class Movie {
    // поля
}

// Використання
Movie movie = Movie.builder()
        .title("Inception")
        .description("A mind-bending thriller")
        .releaseYear(2010)
        .viewCount(0L)
        .build();
```

**Використання:**
- Створення Entity об'єктів
- Створення DTO об'єктів
- Ініціалізація тестових даних

**Переваги:**
- Читабельний код
- Гнучкість при створенні об'єктів
- Захист від помилок (не можна забути встановити обов'язкове поле)

---

## 6. Singleton Pattern (Одиночка)

### Призначення
Гарантує, що клас має тільки один екземпляр і надає глобальну точку доступу до нього.

### Реалізація в проекті

**Місцезнаходження:** Spring Framework автоматично реалізує Singleton через `@Component`, `@Service`, `@Repository`

**Призначення:** Забезпечення єдиного екземпляра сервісів та компонентів.

**Приклади:**
```java
@Service  // Spring створює один екземпляр
public class MovieService {
    // ...
}

@Component  // Spring створює один екземпляр
public class ContentFactory {
    // ...
}
```

**Переваги:**
- Контрольований доступ до єдиного екземпляра
- Глобальна точка доступу
- Лінива ініціалізація (якщо потрібно)

---

## 7. Adapter Pattern (Адаптер)

### Призначення
Перетворює інтерфейс класу в інший інтерфейс, очікуваний клієнтом. Адаптер дозволяє класам працювати разом, що не могло б бути інакше через несумісність інтерфейсів.

### Реалізація в проекті

**Місцезнаходження:** `src/main/java/com/onlinecinema/security/CustomUserDetailsService.java`

**Призначення:** Адаптація User entity до Spring Security UserDetails.

**Код:**
```java
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
```

**Переваги:**
- Інтеграція з Spring Security
- Розділення відповідальностей
- Дотримання Single Responsibility Principle

---

## 8. Facade Pattern (Фасад)

### Призначення
Надає уніфікований інтерфейс до набору інтерфейсів у підсистемі. Фасад визначає інтерфейс вищого рівня, який спрощує використання підсистеми.

### Реалізація в проекті

**Місцезнаходження:** `src/main/java/com/onlinecinema/controller/*.java`

**Призначення:** Контролери надають спрощений REST API інтерфейс до складної бізнес-логіки.

**Приклад:**
```java
@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;
    private final ViewingHistoryService viewingHistoryService;
    
    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        return ResponseEntity.ok(movieService.findAll());
    }
    
    @PostMapping("/{id}/watch")
    public ResponseEntity<ViewingHistoryDto> watchMovie(@PathVariable Long id) {
        // Складна логіка прихована за простим API
        Long userId = securityUtils.getCurrentUserId();
        movieService.incrementViewCount(id);
        ViewingHistoryDto history = viewingHistoryService.recordMovieView(userId, id);
        return ResponseEntity.ok(history);
    }
}
```

**Переваги:**
- Спрощений інтерфейс для клієнтів
- Приховування складності підсистеми
- Легше підтримувати та тестувати

---

## Взаємодія патернів

### Комбінація Factory + Strategy
- `SubscriptionStrategyFactory` (Factory) створює стратегії (Strategy)
- `SubscriptionService` використовує Factory для отримання потрібної стратегії

### Комбінація Observer + Repository
- `RecommendationSubject` (Observer) сповіщає спостерігачів про події
- `RecommendationObserver` використовує Repository для отримання даних

### Комбінація Builder + Factory
- `ContentFactory` (Factory) використовує Builder для створення об'єктів
- Забезпечує гнучкість та читабельність коду

---

## Висновок

Проект використовує 8 основних GoF-патернів:
1. **Factory Pattern** - 2 реалізації (ContentFactory, SubscriptionStrategyFactory)
2. **Strategy Pattern** - для типів підписок
3. **Observer Pattern** - для системи рекомендацій
4. **Repository Pattern** - через Spring Data JPA
5. **Builder Pattern** - через Lombok
6. **Singleton Pattern** - через Spring
7. **Adapter Pattern** - для інтеграції з Spring Security
8. **Facade Pattern** - через REST контролери

Всі патерни працюють разом, забезпечуючи гнучкість, розширюваність та підтримуваність системи.

