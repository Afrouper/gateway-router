package de.afrouper.gateway.router;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.composite.CompositeDiscoveryClientAutoConfiguration;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClientAutoConfiguration;

@SpringBootApplication(exclude = {
        SimpleDiscoveryClientAutoConfiguration.class,
        CompositeDiscoveryClientAutoConfiguration.class
})
public class GatewayRouterApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayRouterApplication.class, args);
    }

}
