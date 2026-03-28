package ie.tus.eng.actor_service.repository;

import ie.tus.eng.actor_service.model.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ActorRepository - Spring Data JPA Repository
 *
 * Spring generates the full CRUD implementation at runtime via dynamic proxy.
 * We get findAll(), findById(), save(), deleteById(), count() etc. for free.
 */
public interface ActorRepository extends JpaRepository<Actor, Long> {

    // Custom finder: generates "SELECT * FROM actors WHERE name = ?"
    Actor findByName(String name);
}