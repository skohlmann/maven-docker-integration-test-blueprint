package de.speexx.blueprint.integration.docker.infrastructure.server;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class ServerTest {
 
    @Test
    public void skuFromSegments() {
        final var segments = Arrays.asList("path", "sku", "123");
        assertThat(Server.extractSkuFromPathSegments(segments).get(), is(equalTo("123")));
    }
}
