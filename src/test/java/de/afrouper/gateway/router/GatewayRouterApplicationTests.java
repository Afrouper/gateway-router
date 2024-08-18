package de.afrouper.gateway.router;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
class GatewayRouterApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testTenantIdRouting() {
        webTestClient.post()
                .uri("/test/4711/person/steve")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.url").isEqualTo("https://httpbin.org/anything/test/4711/person/steve");
    }

    @Test
    public void testTenantNameRouting() {
        webTestClient.post()
                .uri("/test/foo/GWGBUEDR/person/steve")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.url").isEqualTo("https://httpbin.org/anything/test/foo/GWGBUEDR/person/steve");
    }
}
