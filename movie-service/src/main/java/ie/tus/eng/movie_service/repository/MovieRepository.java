package ie.tus.eng.movie_service.repository;

import ie.tus.eng.movie_service.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MovieRepository extends JpaRepository<Movie, Long> {
    Movie findByTitle(String title);
}
