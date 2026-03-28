package ie.tus.eng.actor_service.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import ie.tus.eng.actor_service.model.Movie;

/**
 * ActorResponse - Data Transfer Object (DTO)
 *
 * Returned to the client when they request a single actor (GET /actors/{id}).
 * Combines data from both:
 *   - The Actor (from our local database)
 *   - The Movie (fetched from the Movie service via WebClient)
 *
 * @JsonPropertyOrder ensures actor fields appear first in the JSON output.
 * Without this, Jackson orders fields alphabetically which looks messy.
 *
 * Same pattern as Lab 7's StudentResponse.java which combined
 * Student data with Course data fetched from the Course service.
 */
@JsonPropertyOrder({"actorId", "name", "nationality", "movieId",
                    "movieTitle", "movieGenre", "movieReleaseYear"})
public class ActorResponse {

    // --- Actor fields (from local DB) ---
    private Long actorId;
    private String name;
    private String nationality;
    private Long movieId;

    // --- Movie fields (fetched from Movie service via WebClient) ---
    private String movieTitle;
    private String movieGenre;
    private Integer movieReleaseYear;

    public ActorResponse() {}

    /**
     * Used in ActorController after fetching both the actor (local)
     * and the movie (from Movie service) to build the combined response.
     *
     * If movie is null (Movie service was unreachable), the movie fields
     * stay null — graceful degradation, actor data still comes through.
     */
    public ActorResponse(Long actorId, String name, String nationality,
                         Long movieId, Movie movie) {
        this.actorId = actorId;
        this.name = name;
        this.nationality = nationality;
        this.movieId = movieId;

        if (movie != null) {
            this.movieTitle = movie.getTitle();
            this.movieGenre = movie.getGenre();
            this.movieReleaseYear = movie.getReleaseYear();
        }
    }

    public Long getActorId() { return actorId; }
    public void setActorId(Long actorId) { this.actorId = actorId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public Long getMovieId() { return movieId; }
    public void setMovieId(Long movieId) { this.movieId = movieId; }

    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }

    public String getMovieGenre() { return movieGenre; }
    public void setMovieGenre(String movieGenre) { this.movieGenre = movieGenre; }

    public Integer getMovieReleaseYear() { return movieReleaseYear; }
    public void setMovieReleaseYear(Integer movieReleaseYear) { this.movieReleaseYear = movieReleaseYear; }

    @Override
    public String toString() {
        return "ActorResponse [actorId=" + actorId + ", name=" + name +
               ", nationality=" + nationality + ", movieId=" + movieId +
               ", movieTitle=" + movieTitle + ", movieGenre=" + movieGenre +
               ", movieReleaseYear=" + movieReleaseYear + "]";
    }
}