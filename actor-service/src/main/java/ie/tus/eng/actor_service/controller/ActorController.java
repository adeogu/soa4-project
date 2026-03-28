package ie.tus.eng.actor_service.controller;

import ie.tus.eng.actor_service.client.MovieClient;
import ie.tus.eng.actor_service.dto.ActorResponse;
import ie.tus.eng.actor_service.model.Actor;
import ie.tus.eng.actor_service.model.Movie;
import ie.tus.eng.actor_service.repository.ActorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

/**
 * ActorController - REST Controller for Actor service (Service A / Consumer)
 *
 * Key responsibilities:
 *  1. Full CRUD for actors via JPA
 *  2. ETag caching on GET /actors (collection only, as per project spec)
 *  3. GET /actors/{id} calls the Movie service asynchronously via MovieClient
 *     and returns a combined ActorResponse DTO
 */
@RestController
@RequestMapping("/actors")
@CrossOrigin(origins = "*") // allow requests from the HTML client and Postman
public class ActorController {

    private final ActorRepository repository;

    // MovieClient wraps WebClient — injected by Spring because it is a @Component
    private final MovieClient movieClient;

    public ActorController(ActorRepository repository, MovieClient movieClient) {
        this.repository = repository;
        this.movieClient = movieClient;
    }

    // ================================================================
    // GET /actors  -- Retrieve ALL actors (with ETag caching)
    // ================================================================
    /**
     * ETag CACHING (collection only, as per project spec):
     *
     * 1. Compute MD5 hash of the full actor list -> this is the ETag.
     * 2. Send it back in the response: ETag: "abc123"
     * 3. Client stores it and sends next time: If-None-Match: "abc123"
     * 4. Hash matches -> 304 Not Modified, no body, client uses its cache.
     * 5. Hash changed -> 200 OK, fresh list, new ETag.
     *
     * The HTML client shows "200" or "304" in the status bar to demonstrate this.
     */
    @GetMapping
    public ResponseEntity<List<Actor>> getAllActors(
            @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {

        List<Actor> actors = repository.findAll();

        // Compute ETag as MD5 hash of the list content
        String etagValue = "\"" + DigestUtils.md5DigestAsHex(
                actors.toString().getBytes(StandardCharsets.UTF_8)) + "\"";

        // Client ETag matches -> nothing changed -> 304 Not Modified
        if (etagValue.equals(ifNoneMatch)) {
            return ResponseEntity.status(304).eTag(etagValue).build();
        }

        // Changed or first request -> 200 OK with list + new ETag
        return ResponseEntity.ok().eTag(etagValue).body(actors);
    }

    // ================================================================
    // GET /actors/{id}  -- ONE actor + movie details from Movie service
    // ================================================================
    /**
     * SERVICE-TO-SERVICE COMMUNICATION (Async Non-Blocking via WebClient):
     *
     * 1. Look up the Actor in the local database.
     * 2. Call movieClient.getMovieById() — this uses WebClient to call
     *    GET /movies/{movieId} on the Movie service asynchronously.
     * 3. Combine Actor + Movie into an ActorResponse DTO.
     * 4. Return the DTO as JSON.
     *
     * If the Movie service is down, movieClient returns null and the
     * movie fields in ActorResponse will just be null (graceful degradation).
     */
    @GetMapping("/{id}")
    public ResponseEntity<ActorResponse> getOneActor(@PathVariable long id) {
        Optional<Actor> actorOpt = repository.findById(id);

        if (actorOpt.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404
        }

        Actor actor = actorOpt.get();

        // ---- ASYNC NON-BLOCKING CALL TO MOVIE SERVICE ----
        // See MovieClient.java for full explanation of how WebClient works
        Movie movie = movieClient.getMovieById(actor.getMovieId());
        // --------------------------------------------------

        // Combine actor (local) + movie (from Movie service) into one response
        ActorResponse response = new ActorResponse(
                actor.getActorId(),
                actor.getName(),
                actor.getNationality(),
                actor.getMovieId(),
                movie
        );

        return ResponseEntity.ok(response); // 200
    }

    // ================================================================
    // POST /actors  -- Create a new actor
    // ================================================================
    @PostMapping
    public ResponseEntity<Actor> createActor(@RequestBody Actor actor) {
        Actor saved = repository.save(actor);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getActorId())
                .toUri();

        return ResponseEntity.created(location).build(); // 201
    }

    // ================================================================
    // PUT /actors/{id}  -- Update an existing actor
    // ================================================================
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateActor(@RequestBody Actor actor, @PathVariable long id) {
        if (repository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build(); // 404
        }

        actor.setActorId(id);
        repository.save(actor);
        return ResponseEntity.noContent().build(); // 204
    }

    // ================================================================
    // DELETE /actors/{id}  -- Delete a single actor
    // ================================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteActor(@PathVariable long id) {
        if (repository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build(); // 404
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build(); // 204
    }

    // ================================================================
    // DELETE /actors  -- Delete ALL actors
    // ================================================================
    @DeleteMapping
    public ResponseEntity<Object> deleteAllActors() {
        repository.deleteAll();
        return ResponseEntity.noContent().build(); // 204
    }
}