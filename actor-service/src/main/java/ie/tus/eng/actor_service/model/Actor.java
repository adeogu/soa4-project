package ie.tus.eng.actor_service.model;

import jakarta.persistence.*;

@Entity
@Table(name = "actors")
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long actorId; 

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nationality;

    private Long movieId;

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
