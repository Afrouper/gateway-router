package de.afrouper.gateway.router.routing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

@Component
public class DynamicBackendUriFilter implements GlobalFilter, Ordered {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicBackendUriFilter.class);

    private final TenantResolver tenantResolver;

    public DynamicBackendUriFilter(TenantResolver tenantResolver) {
        this.tenantResolver = tenantResolver;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String rawPath = exchange.getRequest().getURI().getPath();
        String requestPath = (rawPath != null && rawPath.length() > 1) ? rawPath.substring(1) : rawPath;

        Map<String, String> uriVariables = ServerWebExchangeUtils.getUriTemplateVariables(exchange);
        String tenantId = uriVariables.get("tenantId");
        String tenantName = uriVariables.get("tenantName");

        LOGGER.debug("{} Extract TenantId '{}' and TenantName '{}' from request with path '{}'",
                exchange.getLogPrefix(), tenantId, tenantName, requestPath);

        return tenantResolver
                .resolveUpstream(tenantId, tenantName, requestPath)
                .switchIfEmpty(Mono.defer(() -> {
                    LOGGER.warn("{} No upstream URI resolved for tenantId='{}', tenantName='{}', path='{}'",
                            exchange.getLogPrefix(), tenantId, tenantName, requestPath);
                    return Mono.empty();
                }))
                .flatMap(upstreamUri -> {
                    URI oldUri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
                    ServerWebExchangeUtils.addOriginalRequestUrl(exchange, exchange.getRequest().getURI());

                    LOGGER.debug("{} Old URI: '{}' - New URI: '{}'",
                            exchange.getLogPrefix(), oldUri, upstreamUri);

                    // Request mit neuer URI mutieren
                    ServerHttpRequest mutatedRequest = exchange.getRequest()
                            .mutate()
                            .uri(upstreamUri)
                            .build();

                    // Exchange-Attribute setzen
                    ServerWebExchange mutatedExchange = exchange.mutate()
                            .request(mutatedRequest)
                            .build();

                    mutatedExchange.getAttributes()
                            .put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, upstreamUri);

                    // Weiter in der Kette
                    return chain.filter(mutatedExchange);
                })
                // Falls kein Upstream aufgelöst wurde, trotzdem Kette ausführen (z.B. 404 später)
                .switchIfEmpty(chain.filter(exchange));
    }

    @Override
    public int getOrder() {
        // Unmittelbar vor NettyRoutingFilter, damit die URI rechtzeitig gesetzt ist
        return Ordered.LOWEST_PRECEDENCE - 1;
    }
}
