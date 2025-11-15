# Online Cinema - Full Stack Application

A web application for streaming movies and TV series online with subscription functionality.

## Technologies

### Backend
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Spring Security** (JWT authentication)
- **H2 Database** (for development) / PostgreSQL (for production)
- **Maven**
- **Lombok**
- **SpringDoc OpenAPI** (Swagger)

## Project Structure

```
src/main/java/com/onlinecinema/
├── config/              # Configuration (Security, OpenAPI)
├── controller/          # REST controllers
├── dto/                 # Data Transfer Objects
├── entity/              # JPA entities
├── exception/           # Exception handling
├── pattern/             # Design patterns implementation
│   ├── factory/
│   ├── observer/
│   └── strategy/
├── repository/          # Spring Data JPA repositories
├── security/            # JWT and Security configuration
└── service/             # Business logic

## Features

### For Users:
- User registration and authentication (JWT)
- Browse movie and series catalog
- Search and filter (by genre, year, country)
- Subscriptions (FREE with ads / PREMIUM without ads)
- Add to favorites
- Recommendations based on viewing history
- Viewing history

### For Administrators:
- CRUD operations for movies and series
- Genre management
- User management
- Report generation:
  - Most popular movies/genres
  - User activity
  - Subscription revenue

## API Endpoints

### Public:
- `POST /api/auth/register` - registration
- `POST /api/auth/login` - login
- `GET /api/movies` - list movies
- `GET /api/series` - list series
- `GET /api/genres` - list genres

### For authenticated users:
- `GET /api/subscriptions/me` - my subscription
- `POST /api/subscriptions/upgrade` - upgrade to premium
- `GET /api/favorites` - my favorites
- `GET /api/recommendations/movies` - movie recommendations
- `GET /api/recommendations/series` - series recommendations

### For administrators:
- `POST /api/admin/movies` - create movie
- `PUT /api/admin/movies/{id}` - update movie
- `DELETE /api/admin/movies/{id}` - delete movie
- `GET /api/reports` - reports

Complete API documentation available at: `http://localhost:8080/swagger-ui.html`

## Getting Started

### Prerequisites

- **Java 17+**
- **Maven 3.6+**
- **Node.js 18+** (for frontend)
- **npm** or **pnpm** (for frontend)

### Backend Setup

1. **Run the application**:
```bash
mvn spring-boot:run
```

2. **Access the application**:
   - API: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - H2 Console: http://localhost:8080/h2-console
     - JDBC URL: `jdbc:h2:mem:cinemadb`
     - Username: `sa`
     - Password: (empty)

### Frontend Setup

1. **Navigate to frontend directory**:
```bash
cd frontend
```

2. **Install dependencies**:
```bash
npm install
# or
pnpm install
```

3. **Run the development server**:
```bash
npm run dev
# or
pnpm dev
```

4. **Access the frontend**:
   - Application: http://localhost:3000

### Test Users

After startup, the following users are automatically created:

- **Administrator**: `admin` / `admin123` (PREMIUM)
- **User**: `user` / `user123` (FREE)

### Test Data

Automatically created on startup:
- **9 genres** (Action, Drama, Comedy, Thriller, Sci-Fi, Horror, Romance, Crime, Fantasy)
- **8 movies** (The Matrix, Inception, The Shawshank Redemption, The Dark Knight, Pulp Fiction, Forrest Gump, The Godfather, Interstellar)
- **2 series** (Breaking Bad, Game of Thrones) with episodes

## Database

### Main Entities:
- **User** - system users
- **Movie** - movies
- **Series** - TV series
- **Episode** - series episodes
- **Genre** - genres
- **Subscription** - user subscriptions
- **ViewingHistory** - viewing history
- **Favorite** - favorite movies/series

## Configuration

Configuration files:
- `application.properties` - main configuration
- `application-dev.properties` - for development
- `application-prod.properties` - for production

## Testing

```bash
mvn test
```

