package de.speexx.blueprint.integration.docker.infrastructure.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


@Testcontainers
public class ServerIT {
    
    static final int PORT = 9080;
    
    @Container
    public final GenericContainer backend = createBackend(Network.newNetwork());  // Network doesn't implement Startable
    
    @Test
    public void simpleUrlFetch() throws Exception {
        final var client = HttpClient.newBuilder()   
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofMillis(2_000))
                .build();
        final var request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://" + backend.getContainerIpAddress() + ":" + backend.getMappedPort(PORT) + "/sku/123"))
                .timeout(Duration.ofMillis(2_000))
                .build();
        final var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertThat(response.statusCode(), is(equalTo(200)));
    }

    private GenericContainer createBackend(final Network network) {
        final var image = resourceAsString("META-INF/docker/de.speexx.blueprint.integration.docker/blueprint-docker-integration/image-name").trim();

        final var container = new GenericContainer(image)
            .withExposedPorts(PORT)
            .withNetwork(network)
            .waitingFor(Wait.forHttp("/sku/123"));

        container.start();
        return container;
    }
    
    private String resourceAsString(final String resourcePath) {
        try (final var resourceStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
             final var reader = new InputStreamReader(resourceStream, Charset.forName("UTF-8"));
             final var buffered = new BufferedReader(reader)) {
            return buffered.lines().collect(Collectors.joining("\n"));
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
