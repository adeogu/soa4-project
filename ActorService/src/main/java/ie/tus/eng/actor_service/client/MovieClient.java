package ie.tus.eng.actor_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import ie.tus.eng.actor_service.model.Movie;

/**
 * MovieClient - Asynchronous Non-Blocking HTTP client for the Movie service
 *
 * -----------------------------------------------------------------------
 * HOW THE ASYNC NON-BLOCKING COMMUNICATION WORKS:
 * -----------------------------------------------------------------------
 * WebClient is Spring's reactive, non-blocking HTTP client (from WebFlux).
 *
 * Unlike RestTemplate (blocking), WebClient does NOT block the thread
 * while waiting for the Movie service to respond. Instead:
 *   1. WebClient sends the HTTP request to the Movie service.
 *   2. It immediately returns a Mono<Movie> - a "promise" of a future value.
 *   3. The thread is FREE to do other work while waiting.
 *   4. When the Movie service responds, the result is processed reactively.
 *
 * We call .block() at the end to get the result synchronously — this bridges
 * the reactive WebClient into our regular Spring MVC (servlet) controller.
 * In a fully reactive app you would return Mono directly without .block().
 *
 * This satisfies the "Request-Response (Asynchronous Non-Blocking)" requirement.
 * -----------------------------------------------------------------------
 *
 * movie.service.url comes from application.properties:
 *   - Local dev: http://localhost:8081
 *   - Docker:    http://movie-service:8081  (set via MOVIE_SERVICE_URL env var)
 */
@Component // Spring-managed bean — gets injected into ActorController
public class MovieClient {

    private final WebClient webClient;

    /**
     * @Value injects movie.service.url from application.properties.
     * In Docker, the MOVIE_SERVICE_URL environment variable overrides it
     * (Spring Boot automatically maps env vars to properties).
     */
    public MovieClient(@Value("${movie.service.url}") String movieServiceUrl) {
        // Build a reusable WebClient with the Movie service base URL
        this.webClient = WebClient.builder()
                .baseUrl(movieServiceUrl)
                .build();
    }

    /**
     * Calls GET /movies/{id} on the Movie service asynchronously.
     *
     * .get()                    - HTTP GET
     * .uri("/movies/{id}", id)  - appends path to base URL
     * .retrieve()               - fires the request
     * .bodyToMono(Movie.class)  - deserializes JSON response into Movie POJO
     * .onErrorReturn(null)      - if Movie service is down, return null
     *                            instead of throwing an exception (graceful degradation)
     * .block()                  - wait for result (bridges reactive -> Spring MVC)
     */
    public Movie getMovieById(Long movieId) {
        return webClient.get()
                .uri("/movies/{id}", movieId)
                .retrieve()
                .bodyToMono(Movie.class)
                .onErrorReturn(null)
                .block();
    }
}