package ie.tus.eng.movie_service.repository;

import ie.tus.eng.movie_service.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * MovieRepository - Spring Data JPA Repository
 *
 * We only declare this interface — Spring generates the full implementation
 * at runtime (dynamic proxy generation).
 *
 * JpaRepository<Movie, Long> gives us for free:
 *   findAll(), findById(), save(), deleteById(), count() etc.
 */
public interface MovieRepository extends JpaRepository<Movie, Long> {

    // Custom finder using Spring naming convention:
    // 'findBy' + field name = "SELECT * FROM movies WHERE title = ?"
    Movie findByTitle(String title);
}