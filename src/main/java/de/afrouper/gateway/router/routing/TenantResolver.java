package de.afrouper.gateway.router.routing;

import reactor.core.publisher.Mono;

import java.net.URI;

public interface TenantResolver {
    Mono<URI> resolveUpstream(String tenantId, String tenantName, String requestPath);
}