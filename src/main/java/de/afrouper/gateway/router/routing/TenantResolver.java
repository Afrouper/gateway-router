package de.afrouper.gateway.router.routing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * PATH -> SYS_ID
 */
@Component
public class TenantResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(TenantResolver.class);

    private final Map<String, URI> tenantsIdMapping = new HashMap<>();
    private final Map<String, URI> tenantsNameMapping = new HashMap<>();

    public TenantResolver() {
        tenantsIdMapping.put("4711", URI.create("https://httpbin.org:443/anything/"));
        tenantsNameMapping.put("GWGBUEDR", URI.create("https://httpbin.org/anything/"));
    }

    public URI resolveUpstreamWithTenantName(String tenantName, String requestPath) {
        return tenantsNameMapping.get(tenantName).resolve(requestPath);
    }

    public URI resolveUpstreamWithTenantId(String tenantId, String requestPath) {
        return tenantsIdMapping.get(tenantId).resolve(requestPath);
    }

}
