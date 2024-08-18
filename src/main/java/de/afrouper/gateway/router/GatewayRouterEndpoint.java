package de.afrouper.gateway.router;

import de.afrouper.gateway.router.locator.DynamicRouteRefresher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id="routes")
public class GatewayRouterEndpoint {

    private final DynamicRouteRefresher dynamicRouteRefresher;

    @Autowired
    public GatewayRouterEndpoint(DynamicRouteRefresher dynamicRouteRefresher) {
        this.dynamicRouteRefresher = dynamicRouteRefresher;
    }

    @WriteOperation
    public void refreshRoutes() {
        dynamicRouteRefresher.refreshRoutes();
    }
}
