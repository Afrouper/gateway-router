package de.afrouper.gateway.router.routing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;
import java.util.Objects;

@Component
public class InMemoryTenantResolver implements TenantResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryTenantResolver.class);

    private static final Map<String, URI> TENANT_ID_MAPPING = Map.of(
            "4711", URI.create("http://httpcan.org:80/anything/")
    );

    private static final Map<String, URI> TENANT_NAME_MAPPING = Map.of(
            "GWGBUEDR", URI.create("http://httpcan.org/anything/")
    );

    @Override
    public Mono<URI> resolveUpstream(String tenantId, String tenantName, String requestPath) {
        return Mono.defer(() -> {
            URI baseUri = tenantId != null
                    ? TENANT_ID_MAPPING.get(tenantId)
                    : tenantName != null
                    ? TENANT_NAME_MAPPING.get(tenantName)
                    : null;

            if (baseUri == null) {
                LOGGER.warn("No base URI found for tenantId='{}', tenantName='{}'", tenantId, tenantName);
                return Mono.empty();
            }

            String effectivePath = Objects.requireNonNullElse(requestPath, "");
            URI resolved = baseUri.resolve(effectivePath);

            LOGGER.debug("Resolved upstream URI '{}' for tenantId='{}', tenantName='{}', path='{}'",
                    resolved, tenantId, tenantName, effectivePath);

            return Mono.just(resolved);
        });
    }
}
