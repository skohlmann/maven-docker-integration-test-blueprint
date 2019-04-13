package de.speexx.blueprint.integration.docker.infrastructure.server;

import io.helidon.webserver.PathMatcher;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerConfiguration;
import io.helidon.webserver.WebServer;
import java.util.List;
import java.util.Optional;
import static java.util.concurrent.TimeUnit.SECONDS;


public class Server {
    
    public static void main(final String[] args) throws Exception {
        final var webServer = WebServer
                .create(ServerConfiguration.builder().port(9080).build(),
                        Routing.builder()
                               .get(PathMatcher.create("/sku/{}"), (req, res) -> {
                                           final var segments = req.path().segments();
                                           if (!segments.isEmpty()) {
                                               res.send("GET works with SKU " + extractSkuFromPathSegments(segments).get() + "!\n");
                                           } else {
                                               res.send("GET works but no SKU!\n");
                                           }
                                       })
                               .put((req, res) -> res.send("PUT works also!\n"))
                               .any((req, res) -> res.send("Something called! " + req.remotePort() + " - " + req.headers().toMap() + "\n"))
                               .build())
                .start() 
                .toCompletableFuture()
                .get(10, SECONDS);
    }
    
    static Optional<String> extractSkuFromPathSegments(final List<String> segments) {
        return Optional.ofNullable(segments.get(segments.size() - 1));
    }
}
