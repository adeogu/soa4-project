package ie.tus.eng.movie_service.model;

import jakarta.persistence.*;

/**
 * Movie - JPA Entity (maps to the 'movies' table in the database)
 *
 * @Entity tells Hibernate to create a 'movies' table based on this class.
 * JPA automatically converts camelCase field names to snake_case column names:
 *   movieId     -> movie_id
 *   releaseYear -> release_year
 */
@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment primary key
    private Long movieId; // Long (not long) so it can be null for POST operations

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    private Integer releaseYear;

    // No-args constructor required by JPA / Hibernate
    public Movie() {}

    public Movie(Long movieId, String title, String genre, Integer releaseYear) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.releaseYear = releaseYear;
    }

    public Long getMovieId() { return movieId; }
    public void setMovieId(Long movieId) { this.movieId = movieId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public Integer getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }

    @Override
    public String toString() {
        return "Movie [movieId=" + movieId + ", title=" + title +
               ", genre=" + genre + ", releaseYear=" + releaseYear + "]";
    }
}