package ie.tus.eng.movie_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MovieServiceApplication - Service B (Producer)
 *
 * This is the Movie microservice. It acts as the "producer" in the
 * Request-Response communication pattern. The Actor service (Service A)
 * will call this service to retrieve movie details.
 *
 * Runs on port 8081 (see application.properties).
 */
@SpringBootApplication
public class MovieServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MovieServiceApplication.class, args);
    }
}