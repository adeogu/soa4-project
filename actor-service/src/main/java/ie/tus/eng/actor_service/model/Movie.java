package ie.tus.eng.actor_service.model;

public class Movie {

    private Long movieId;
    private String title;
    private String genre;
    private Integer releaseYear;

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
