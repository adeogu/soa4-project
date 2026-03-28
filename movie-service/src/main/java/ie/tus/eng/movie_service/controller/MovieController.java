package ie.tus.eng.movie_service.controller;

import ie.tus.eng.movie_service.model.Movie;
import ie.tus.eng.movie_service.repository.MovieRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

/**
 * MovieController - REST Controller for Movie service (Service B / Producer)
 *
 * Full CRUD for movies.
 * ETag caching is implemented on GET /movies (collection only) as per the project spec.
 */
@RestController
@RequestMapping("/movies")
@CrossOrigin(origins = "*") // allow requests from actor-service HTML client
public class MovieController {

    private final MovieRepository repository;

    public MovieController(MovieRepository repository) {
        this.repository = repository;
    }

    // ================================================================
    // GET /movies  -- Retrieve ALL movies (with ETag caching)
    // ================================================================
    /**
     * ETag CACHING EXPLAINED:
     * 1. We compute an MD5 hash of the entire movie list.
     * 2. We send it as an ETag header: ETag: "abc123"
     * 3. Client stores it and sends back: If-None-Match: "abc123" next time.
     * 4. If hash matches -> 304 Not Modified (no body, client uses cache).
     * 5. If hash changed -> 200 OK with fresh data + new ETag.
     */
    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies(
            @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {

        List<Movie> movies = repository.findAll();

        // Compute ETag as MD5 hash of the list content
        String etagValue = "\"" + DigestUtils.md5DigestAsHex(
                movies.toString().getBytes(StandardCharsets.UTF_8)) + "\"";

        // If client ETag matches -> nothing changed -> 304 Not Modified
        if (etagValue.equals(ifNoneMatch)) {
            return ResponseEntity.status(304).eTag(etagValue).build();
        }

        // Changed or first request -> 200 OK with list + new ETag
        return ResponseEntity.ok().eTag(etagValue).body(movies);
    }

    // ================================================================
    // GET /movies/{id}  -- Retrieve ONE movie (no ETag on single items)
    // ================================================================
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getOneMovie(@PathVariable long id) {
        Optional<Movie> movie = repository.findById(id);
        if (movie.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404
        }
        return ResponseEntity.ok(movie.get()); // 200
    }

    // ================================================================
    // POST /movies  -- Create a new movie
    // ================================================================
    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        Movie saved = repository.save(movie);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getMovieId())
                .toUri();

        return ResponseEntity.created(location).build(); // 201 Created
    }

    // ================================================================
    // PUT /movies/{id}  -- Update an existing movie
    // ================================================================
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMovie(@RequestBody Movie movie, @PathVariable long id) {
        if (repository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build(); // 404
        }
        movie.setMovieId(id);
        repository.save(movie);
        return ResponseEntity.noContent().build(); // 204
    }

    // ================================================================
    // DELETE /movies/{id}  -- Delete a single movie
    // ================================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMovie(@PathVariable long id) {
        if (repository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build(); // 404
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build(); // 204
    }

    // ================================================================
    // DELETE /movies  -- Delete ALL movies
    // ================================================================
    @DeleteMapping
    public ResponseEntity<Object> deleteAllMovies() {
        repository.deleteAll();
        return ResponseEntity.noContent().build(); // 204
    }
}