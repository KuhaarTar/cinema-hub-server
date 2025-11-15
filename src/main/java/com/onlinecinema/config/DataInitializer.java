package com.onlinecinema.config;

import com.onlinecinema.entity.*;
import com.onlinecinema.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository;
    private final SeriesRepository seriesRepository;
    private final EpisodeRepository episodeRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            System.out.println("Database already initialized. Skipping data initialization.");
            return;
        }

        System.out.println("Initializing database with sample data...");

        Genre action = createGenre("Action", "Action-packed movies with thrilling sequences");
        Genre drama = createGenre("Drama", "Emotional and character-driven stories");
        Genre comedy = createGenre("Comedy", "Funny and entertaining movies");
        Genre thriller = createGenre("Thriller", "Suspenseful and exciting movies");
        Genre sciFi = createGenre("Sci-Fi", "Science fiction movies");
        Genre horror = createGenre("Horror", "Scary and suspenseful movies");
        Genre romance = createGenre("Romance", "Love stories and romantic movies");
        Genre crime = createGenre("Crime", "Crime and criminal stories");
        Genre fantasy = createGenre("Fantasy", "Fantasy and magical stories");

        User admin = User.builder()
                .username("admin")
                .email("admin@cinema.com")
                .password(passwordEncoder.encode("admin123"))
                .firstName("Admin")
                .lastName("User")
                .role(User.Role.ADMIN)
                .build();
        admin = userRepository.save(admin);

        Subscription adminSub = Subscription.builder()
                .user(admin)
                .type(Subscription.SubscriptionType.PREMIUM)
                .isActive(true)
                .price(9.99)
                .build();
        subscriptionRepository.save(adminSub);

        User user = User.builder()
                .username("user")
                .email("user@cinema.com")
                .password(passwordEncoder.encode("user123"))
                .firstName("John")
                .lastName("Doe")
                .role(User.Role.USER)
                .build();
        user = userRepository.save(user);

        Subscription userSub = Subscription.builder()
                .user(user)
                .type(Subscription.SubscriptionType.FREE)
                .isActive(true)
                .price(0.0)
                .build();
        subscriptionRepository.save(userSub);

        Movie movie1 = Movie.builder()
                .title("The Matrix")
                .description("A computer hacker learns about the true nature of reality and his role in the war against its controllers.")
                .releaseDate(LocalDate.of(1999, 3, 31))
                .releaseYear(1999)
                .country("USA")
                .posterUrl("https://image.tmdb.org/t/p/w500/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg")
                .videoUrl("https://www.youtube.com/embed/vKQi3bBA1y8")
                .durationMinutes(136)
                .rating(8.7)
                .viewCount(1500000L)
                .genres(Set.of(action, sciFi, thriller))
                .build();
        movieRepository.save(movie1);

        Movie movie2 = Movie.builder()
                .title("Inception")
                .description("A skilled thief is given a chance at redemption if he can pull off an impossible heist.")
                .releaseDate(LocalDate.of(2010, 7, 16))
                .releaseYear(2010)
                .country("USA")
                .posterUrl("https://image.tmdb.org/t/p/w500/oYuLEt3zVCKq57qu2F8dT7NIa6f.jpg")
                .videoUrl("https://www.youtube.com/embed/YoHD9XEInc0")
                .durationMinutes(148)
                .rating(8.8)
                .viewCount(2000000L)
                .genres(Set.of(action, sciFi, thriller))
                .build();
        movieRepository.save(movie2);

        Movie movie3 = Movie.builder()
                .title("The Shawshank Redemption")
                .description("Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.")
                .releaseDate(LocalDate.of(1994, 9, 23))
                .releaseYear(1994)
                .country("USA")
                .posterUrl("https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg")
                .videoUrl("https://www.youtube.com/embed/6hB3S9bIaco")
                .durationMinutes(142)
                .rating(9.3)
                .viewCount(5000000L)
                .genres(Set.of(drama))
                .build();
        movieRepository.save(movie3);

        Movie movie4 = Movie.builder()
                .title("The Dark Knight")
                .description("When the menace known as the Joker wreaks havoc on Gotham, Batman must accept one of the greatest psychological tests of his ability to fight injustice.")
                .releaseDate(LocalDate.of(2008, 7, 18))
                .releaseYear(2008)
                .country("USA")
                .posterUrl("https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg")
                .videoUrl("https://www.youtube.com/embed/EXeTwQWrcwY")
                .durationMinutes(152)
                .rating(9.0)
                .viewCount(3000000L)
                .genres(Set.of(action, drama, thriller))
                .build();
        movieRepository.save(movie4);

        Movie movie5 = Movie.builder()
                .title("Pulp Fiction")
                .description("The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption.")
                .releaseDate(LocalDate.of(1994, 10, 14))
                .releaseYear(1994)
                .country("USA")
                .posterUrl("https://image.tmdb.org/t/p/w500/d5iIlFn5s0ImszYzBPb8JPIfbXD.jpg")
                .videoUrl("https://www.youtube.com/embed/s7EdQ4FqbhY")
                .durationMinutes(154)
                .rating(8.9)
                .viewCount(2500000L)
                .genres(Set.of(crime, thriller))
                .build();
        movieRepository.save(movie5);

        Movie movie6 = Movie.builder()
                .title("Forrest Gump")
                .description("The presidencies of Kennedy and Johnson, the Vietnam War, the Watergate scandal and other historical events unfold from the perspective of an Alabama man with an IQ of 75.")
                .releaseDate(LocalDate.of(1994, 7, 6))
                .releaseYear(1994)
                .country("USA")
                .posterUrl("https://image.tmdb.org/t/p/w500/arw2vcBveWOVZr6pxd9XTd1TdQa.jpg")
                .videoUrl("https://www.youtube.com/embed/bLvqoHBptjg")
                .durationMinutes(142)
                .rating(8.8)
                .viewCount(4000000L)
                .genres(Set.of(drama, romance))
                .build();
        movieRepository.save(movie6);

        Movie movie7 = Movie.builder()
                .title("The Godfather")
                .description("The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.")
                .releaseDate(LocalDate.of(1972, 3, 24))
                .releaseYear(1972)
                .country("USA")
                .posterUrl("https://image.tmdb.org/t/p/w500/3bhkrj58Vtu7enYsRolD1fZdja1.jpg")
                .videoUrl("https://www.youtube.com/embed/UaVTIH8mujA")
                .durationMinutes(175)
                .rating(9.2)
                .viewCount(6000000L)
                .genres(Set.of(crime, drama))
                .build();
        movieRepository.save(movie7);

        Movie movie8 = Movie.builder()
                .title("Interstellar")
                .description("A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.")
                .releaseDate(LocalDate.of(2014, 11, 7))
                .releaseYear(2014)
                .country("USA")
                .posterUrl("https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg")
                .videoUrl("https://www.youtube.com/embed/zSWdZVtXT7E")
                .durationMinutes(169)
                .rating(8.6)
                .viewCount(1800000L)
                .genres(Set.of(drama, sciFi))
                .build();
        movieRepository.save(movie8);

        // Create series with real poster URLs
        Series series1 = Series.builder()
                .title("Breaking Bad")
                .description("A high school chemistry teacher turned methamphetamine manufacturer partners with a former student.")
                .releaseDate(LocalDate.of(2008, 1, 20))
                .releaseYear(2008)
                .country("USA")
                .posterUrl("https://image.tmdb.org/t/p/w500/ggFHVNu6YYI5L9pCfOacjizRGt.jpg")
                .rating(9.5)
                .viewCount(8000000L)
                .genres(Set.of(crime, drama, thriller))
                .build();
        series1 = seriesRepository.save(series1);

        // Add episodes for Breaking Bad with YouTube trailer URLs
        Episode episode1 = Episode.builder()
                .series(series1)
                .season(1)
                .episodeNumber(1)
                .title("Pilot")
                .description("A high school chemistry teacher discovers he has cancer and decides to enter the drug trade.")
                .videoUrl("https://www.youtube.com/embed/HhesaQXLuRY")
                .durationMinutes(58)
                .viewCount(500000L)
                .build();
        episodeRepository.save(episode1);

        Episode episode2 = Episode.builder()
                .series(series1)
                .season(1)
                .episodeNumber(2)
                .title("Cat's in the Bag...")
                .description("Walt and Jesse try to dispose of two bodies.")
                .videoUrl("https://www.youtube.com/embed/HhesaQXLuRY")
                .durationMinutes(48)
                .viewCount(450000L)
                .build();
        episodeRepository.save(episode2);

        Series series2 = Series.builder()
                .title("Game of Thrones")
                .description("Nine noble families fight for control over the lands of Westeros.")
                .releaseDate(LocalDate.of(2011, 4, 17))
                .releaseYear(2011)
                .country("USA")
                .posterUrl("https://image.tmdb.org/t/p/w500/u3bZgnGQ9T01sWNhyveQz0wH0Hl.jpg")
                .rating(9.3)
                .viewCount(12000000L)
                .genres(Set.of(action, drama, fantasy))
                .build();
        series2 = seriesRepository.save(series2);

        Episode gotEpisode1 = Episode.builder()
                .series(series2)
                .season(1)
                .episodeNumber(1)
                .title("Winter Is Coming")
                .description("Ned Stark becomes the Hand of the King.")
                .videoUrl("https://www.youtube.com/embed/BpJYNVhGf1s")
                .durationMinutes(62)
                .viewCount(800000L)
                .build();
        episodeRepository.save(gotEpisode1);

        System.out.println("Database initialized successfully!");
        System.out.println("Admin credentials: username=admin, password=admin123");
        System.out.println("User credentials: username=user, password=user123 (Free)");
    }

    private Genre createGenre(String name, String description) {
        Genre genre = new Genre();
        genre.setName(name);
        genre.setDescription(description);
        return genreRepository.save(genre);
    }
}

