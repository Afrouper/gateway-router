package de.afrouper.gateway.router.locator;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RouteServiceImpl implements RouteService {
    //ToDo: Use Repository
    //https://anjireddy-kata.medium.com/spring-cloud-gateway-dynamic-route-configuration-and-loading-from-the-datastore-a0637e6bd9b4

    @Override
    public Flux<ApiRoute> getAll() {
        return Flux.just(createApi(), createApi2(), createApi3());
        //return this.routeRepository.findAll();
    }

    private ApiRoute createApi() {
        ApiRoute route = new ApiRoute();
        route.setMethod("GET");
        route.setPath("/*");
        route.setUri("https://httpbin.org");
        return route;
    }

    private ApiRoute createApi2() {
        ApiRoute route = new ApiRoute();
        route.setMethod("POST");
        route.setPath("/test/{tenantId}/person/*");
        route.setUri("https://httpbin.org");
        return route;
    }

    private ApiRoute createApi3() {
        ApiRoute route = new ApiRoute();
        route.setMethod("POST");
        route.setPath("/test/foo/{tenantName}/person/*");
        route.setUri("https://httpbin.org");
        return route;
    }


    @Override
    public Mono<ApiRoute> create(ApiRoute apiRoute) {
        return Mono.empty();
        //return this.routeRepository.save(apiRoute);
    }

    @Override
    public Mono<ApiRoute> getById(String id) {
        return Mono.empty();
        //return this.routeRepository.findById(id);
    }
}
