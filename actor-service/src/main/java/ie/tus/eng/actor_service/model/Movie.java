package ie.tus.eng.actor_service.model;

/**
 * Movie - Plain Old Java Object (POJO) in the Actor service
 *
 * This is NOT a JPA entity — no @Entity, @Table, @Id etc.
 * Its only job is to deserialize the JSON that comes back from the
 * Movie service when WebClient calls GET /movies/{id}.
 *
 * This is exactly the same pattern as Lab 7, where Course.java was
 * copied into the Student service with all JPA annotations removed.
 */
public class Movie {

    private Long movieId;
    private String title;
    private String genre;
    private Integer releaseYear;

    // No-args constructor needed by Jackson to deserialize JSON -> object
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