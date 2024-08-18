package de.afrouper.gateway.router.routing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class DynamicBackendUriFilter implements GlobalFilter, Ordered {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicBackendUriFilter.class);

    private final TenantResolver tenantResolver;

    @Autowired
    public DynamicBackendUriFilter(TenantResolver tenantResolver) {
        this.tenantResolver = tenantResolver;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestPath = exchange.getRequest().getURI().getPath();
        if(requestPath != null && requestPath.length() > 1) {
            requestPath = requestPath.substring(1);
        }
        String tenantId = ServerWebExchangeUtils.getUriTemplateVariables(exchange).get("tenantId");
        String tenantName = ServerWebExchangeUtils.getUriTemplateVariables(exchange).get("tenantName");
        LOGGER.debug("{} Extract TenantId '{}' and TenantName '{}' from request with path '{}'", exchange.getLogPrefix(), tenantId, tenantName, requestPath);

        URI upstreamUri = null;
        if(tenantId != null) {
            upstreamUri = tenantResolver.resolveUpstreamWithTenantId(tenantId, requestPath);
        } else if (tenantName != null) {
            upstreamUri = tenantResolver.resolveUpstreamWithTenantName(tenantName, requestPath);
        }

        URI oldUri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        ServerWebExchangeUtils.addOriginalRequestUrl(exchange, exchange.getRequest().getURI());
        LOGGER.debug("{} Old URI: '{}' - New URI: '{}'", exchange.getLogPrefix(), oldUri, upstreamUri);
        exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, upstreamUri);

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
