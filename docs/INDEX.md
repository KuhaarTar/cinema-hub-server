# Документація проекту Online Cinema

## 📋 Зміст

### 1. Use Cases Діаграма
📄 [USE_CASES.md](USE_CASES.md)

- Діаграма Use Cases (Mermaid)
- Детальний опис 26 Use Cases
- Взаємодія акторів (Користувач, Адміністратор, Система)
- Опис основних та альтернативних сценаріїв

### 2. UML-діаграма класів
📄 [UML_CLASS_DIAGRAM.md](UML_CLASS_DIAGRAM.md)

- Повна UML-діаграма класів (Mermaid)
- Опис Entity Layer (Сутності)
- Опис DTO Layer (Data Transfer Objects)
- Опис Service Layer (Бізнес-логіка)
- Опис Controller Layer (REST API)
- Опис Repository Layer (Доступ до даних)
- Опис Design Patterns
- Відносини між класами

### 3. GoF-патерни проектування
📄 [DESIGN_PATTERNS_DETAILED.md](DESIGN_PATTERNS_DETAILED.md)

Детальна документація 8 GoF-патернів:

1. **Factory Pattern**
   - ContentFactory: `src/main/java/com/onlinecinema/pattern/factory/ContentFactory.java`
   - SubscriptionStrategyFactory: `src/main/java/com/onlinecinema/pattern/strategy/SubscriptionStrategyFactory.java`

2. **Strategy Pattern**
   - Інтерфейс: `src/main/java/com/onlinecinema/pattern/strategy/SubscriptionStrategy.java`
   - Реалізації: `FreeSubscriptionStrategy.java`, `PremiumSubscriptionStrategy.java`
   - Фабрика: `SubscriptionStrategyFactory.java`

3. **Observer Pattern**
   - Інтерфейс: `src/main/java/com/onlinecinema/pattern/observer/RecommendationObserver.java`
   - Суб'єкт: `src/main/java/com/onlinecinema/pattern/observer/RecommendationSubject.java`
   - Спостерігач: `src/main/java/com/onlinecinema/pattern/observer/GenreBasedRecommendationObserver.java`

4. **Repository Pattern**
   - Всі репозиторії: `src/main/java/com/onlinecinema/repository/*.java`

5. **Builder Pattern**
   - Всі Entity та DTO класи з анотацією `@Builder`

6. **Singleton Pattern**
   - Всі сервіси та компоненти через Spring `@Component`, `@Service`, `@Repository`

7. **Adapter Pattern**
   - `src/main/java/com/onlinecinema/security/CustomUserDetailsService.java`

8. **Facade Pattern**
   - Всі REST контролери: `src/main/java/com/onlinecinema/controller/*.java`

### 4. GRASP та SOLID принципи
📄 [GRASP_SOLID.md](GRASP_SOLID.md)

#### GRASP Патерни:
- **Information Expert** - приклади з MovieService, UserService, SubscriptionService
- **Creator** - ContentFactory, Repository
- **Controller** - REST контролери
- **Low Coupling** - Dependency Injection через інтерфейси
- **High Cohesion** - чітке розділення відповідальностей

#### SOLID Принципи:
- **Single Responsibility Principle (SRP)**
- **Open/Closed Principle (OCP)**
- **Liskov Substitution Principle (LSP)**
- **Interface Segregation Principle (ISP)**
- **Dependency Inversion Principle (DIP)**

## 🗂️ Структура файлів

```
docs/
├── INDEX.md                          # Цей файл - індекс документації
├── USE_CASES.md                      # Use Cases діаграма та опис
├── UML_CLASS_DIAGRAM.md              # UML-діаграма класів
├── DESIGN_PATTERNS_DETAILED.md       # Детальна документація GoF-патернів
└── GRASP_SOLID.md                    # GRASP та SOLID принципи
```

## 📍 Швидкий доступ до патернів

### Factory Pattern
- `src/main/java/com/onlinecinema/pattern/factory/ContentFactory.java`
- `src/main/java/com/onlinecinema/pattern/strategy/SubscriptionStrategyFactory.java`

### Strategy Pattern
- `src/main/java/com/onlinecinema/pattern/strategy/SubscriptionStrategy.java`
- `src/main/java/com/onlinecinema/pattern/strategy/FreeSubscriptionStrategy.java`
- `src/main/java/com/onlinecinema/pattern/strategy/PremiumSubscriptionStrategy.java`

### Observer Pattern
- `src/main/java/com/onlinecinema/pattern/observer/RecommendationObserver.java`
- `src/main/java/com/onlinecinema/pattern/observer/RecommendationSubject.java`
- `src/main/java/com/onlinecinema/pattern/observer/GenreBasedRecommendationObserver.java`

### Repository Pattern
- `src/main/java/com/onlinecinema/repository/MovieRepository.java`
- `src/main/java/com/onlinecinema/repository/UserRepository.java`
- `src/main/java/com/onlinecinema/repository/SubscriptionRepository.java`
- ... (всі інші репозиторії)

### Builder Pattern
- Всі Entity: `src/main/java/com/onlinecinema/entity/*.java`
- Всі DTO: `src/main/java/com/onlinecinema/dto/*.java`

### Singleton Pattern
- Всі сервіси: `src/main/java/com/onlinecinema/service/*.java`
- Всі компоненти: `src/main/java/com/onlinecinema/pattern/**/*.java`

### Adapter Pattern
- `src/main/java/com/onlinecinema/security/CustomUserDetailsService.java`

### Facade Pattern
- Всі контролери: `src/main/java/com/onlinecinema/controller/*.java`

## 📊 Діаграми

Всі діаграми створені у форматі Mermaid, який підтримується:
- GitHub (автоматичне відображення)
- VS Code (з розширенням Mermaid)
- Онлайн редактори (mermaid.live)
- Документація (GitBook, MkDocs)

## 🔍 Як використовувати документацію

1. **Для розуміння системи:** Почніть з [USE_CASES.md](USE_CASES.md)
2. **Для розуміння архітектури:** Перегляньте [UML_CLASS_DIAGRAM.md](UML_CLASS_DIAGRAM.md)
3. **Для розуміння патернів:** Вивчіть [DESIGN_PATTERNS_DETAILED.md](DESIGN_PATTERNS_DETAILED.md)
4. **Для розуміння принципів:** Прочитайте [GRASP_SOLID.md](GRASP_SOLID.md)

## 📝 Примітки

- Всі діаграми можна переглянути на GitHub або в редакторах з підтримкою Mermaid
- Кожен патерн має посилання на конкретні файли в проекті
- Приклади коду наведені для кращого розуміння
- Всі принципи проілюстровані конкретними прикладами з проекту

