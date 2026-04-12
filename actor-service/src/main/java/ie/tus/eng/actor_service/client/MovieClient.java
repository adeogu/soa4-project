package ie.tus.eng.actor_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import ie.tus.eng.actor_service.model.Movie;
import reactor.core.publisher.Mono;

 
@Component 
public class MovieClient {

    private final WebClient webClient;

   
    public MovieClient(@Value("${movie.service.url}") String movieServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(movieServiceUrl)
                .build();
    }


    public Movie getMovieById(Long movieId) {
        return webClient.get()
                .uri("/movies/{id}", movieId)
                .retrieve()
                .bodyToMono(Movie.class)
                .onErrorResume(e -> Mono.empty())  
                .blockOptional()                    
                .orElse(null);                      // null if empty
    }
}
