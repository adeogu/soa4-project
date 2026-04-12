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


@RestController
@RequestMapping("/actors")
@CrossOrigin(origins = "*") 
public class ActorController {

    private final ActorRepository repository;

    private final MovieClient movieClient;

    public ActorController(ActorRepository repository, MovieClient movieClient) {
        this.repository = repository;
        this.movieClient = movieClient;
    }

    // GET /actors  -- Retrieve ALL actors (with ETag caching)

    @GetMapping
    public ResponseEntity<List<Actor>> getAllActors(
            @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {

        List<Actor> actors = repository.findAll();

        String etagValue = "\"" + DigestUtils.md5DigestAsHex(
                actors.toString().getBytes(StandardCharsets.UTF_8)) + "\"";

        if (etagValue.equals(ifNoneMatch)) {
            return ResponseEntity.status(304).eTag(etagValue).build();
        }

        return ResponseEntity.ok().eTag(etagValue).body(actors);
    }

    // GET /actors/{id}  -- ONE actor + movie details from Movie service
   
    @GetMapping("/{id}")
    public ResponseEntity<ActorResponse> getOneActor(@PathVariable long id) {
        Optional<Actor> actorOpt = repository.findById(id);

        if (actorOpt.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404
        }

        Actor actor = actorOpt.get();

        Movie movie = movieClient.getMovieById(actor.getMovieId());
        // --------------------------------------------------

        ActorResponse response = new ActorResponse(
                actor.getActorId(),
                actor.getName(),
                actor.getNationality(),
                actor.getMovieId(),
                movie
        );

        return ResponseEntity.ok(response); // 200
    }

    // POST /actors  -- Create a new actor
    @PostMapping
    public ResponseEntity<Actor> createActor(@RequestBody Actor actor) {
        Actor saved = repository.save(actor);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getActorId())
                .toUri();

        return ResponseEntity.created(location).build(); // 201
    }

    // PUT /actors/{id}  -- Update an existing actor
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateActor(@RequestBody Actor actor, @PathVariable long id) {
        if (repository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build(); // 404
        }

        actor.setActorId(id);
        repository.save(actor);
        return ResponseEntity.noContent().build(); // 204
    }

    // DELETE /actors/{id}  -- Delete a single actor
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteActor(@PathVariable long id) {
        if (repository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build(); // 404
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build(); // 204
    }

    // DELETE /actors  -- Delete ALL actors
    @DeleteMapping
    public ResponseEntity<Object> deleteAllActors() {
        repository.deleteAll();
        return ResponseEntity.noContent().build(); // 204
    }
}
