package de.afrouper.gateway.router;

import org.testcontainers.consul.ConsulContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Testcontainers
class GatewayRouterApplicationTests {

    @Container
    static ConsulContainer consulContainer = new ConsulContainer("consul:latest");

    @Autowired
    private WebTestClient webTestClient;

    @BeforeAll
    public static void setUp() {
        System.setProperty("spring.cloud.consul.host", consulContainer.getHost());
        System.setProperty("spring.cloud.consul.port", String.valueOf(consulContainer.getFirstMappedPort()));
    }

    @Test
    void testTenantIdRouting() {
        webTestClient.post()
                .uri("/test/4711/person/steve")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.url")
                .isEqualTo("http://httpcan.org/anything/test/4711/person/steve");
    }

    @Test
    void testTenantNameRouting() {
        webTestClient.post()
                .uri("/test/foo/GWGBUEDR/person/steve")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.url")
                .isEqualTo("http://httpcan.org/anything/test/foo/GWGBUEDR/person/steve");
    }
}
