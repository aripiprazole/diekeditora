package com.diekeditora.web.tests.utils;

import org.jetbrains.annotations.NotNull;
import org.springframework.test.web.reactive.server.MockServerConfigurer;
import org.springframework.test.web.reactive.server.WebTestClient.MockServerSpec;

public final class WebTestClientUtils {
    @NotNull
    public static <T extends MockServerSpec<T>> MockServerSpec<T>
    apply(@NotNull final MockServerSpec<T> spec, @NotNull final MockServerConfigurer configurer) {
        return spec.apply(configurer);
    }
}
