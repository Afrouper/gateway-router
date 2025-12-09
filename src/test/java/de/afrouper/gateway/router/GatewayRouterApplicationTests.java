package de.afrouper.gateway.router;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class GatewayRouterApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

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
