package ie.tus.eng.actor_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ActorServiceApplication - Service A (Consumer)
 *
 * This is the Actor microservice. It acts as the "consumer" in the
 * Request-Response (Asynchronous Non-Blocking) communication pattern.
 *
 * When a client requests a single actor, this service asynchronously
 * calls the Movie service (Service B) using WebClient to fetch the
 * associated movie details and returns them together in an ActorResponse DTO.
 *
 * Runs on port 8080 (see application.properties).
 * The HTML/JS client is served from src/main/resources/static/index.html.
 */
@SpringBootApplication
public class ActorServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ActorServiceApplication.class, args);
    }
}