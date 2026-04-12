package ie.tus.eng.actor_service.repository;

import ie.tus.eng.actor_service.model.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRepository extends JpaRepository<Actor, Long> {
    Actor findByName(String name);
}
