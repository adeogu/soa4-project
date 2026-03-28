package ie.tus.eng.actor_service.model;

import jakarta.persistence.*;

/**
 * Actor - JPA Entity (maps to the 'actors' table in the database)
 *
 * JPA auto-converts camelCase to snake_case:
 *   actorId -> actor_id
 *   movieId -> movie_id
 *
 * movieId is a plain Long (not a @ManyToOne) because the Movie table
 * lives in a completely separate service and database — JPA can't
 * manage a foreign key across two different databases.
 */
@Entity
@Table(name = "actors")
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment
    private Long actorId; // Long (not long) so it can be null on POST

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nationality;

    // Cross-service reference — stored as a plain value, not a JPA join
    private Long movieId;

    // No-args constructor required by JPA
    public Actor() {}

    public Actor(Long actorId, String name, String nationality, Long movieId) {
        this.actorId = actorId;
        this.name = name;
        this.nationality = nationality;
        this.movieId = movieId;
    }

    public Long getActorId() { return actorId; }
    public void setActorId(Long actorId) { this.actorId = actorId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public Long getMovieId() { return movieId; }
    public void setMovieId(Long movieId) { this.movieId = movieId; }

    @Override
    public String toString() {
        return "Actor [actorId=" + actorId + ", name=" + name +
               ", nationality=" + nationality + ", movieId=" + movieId + "]";
    }
}