package de.afrouper.gateway.router.locator;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RouteService {
    Flux<ApiRoute> getAll();

    Mono<ApiRoute> create(ApiRoute apiRoute);

    Mono<ApiRoute> getById(String id);
}
