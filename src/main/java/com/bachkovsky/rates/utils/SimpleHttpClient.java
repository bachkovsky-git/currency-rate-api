package com.bachkovsky.rates.utils;

import javax.validation.constraints.NotNull;
import java.util.Map;

@FunctionalInterface
public interface SimpleHttpClient {

    @NotNull
    String makeRequest(@NotNull String url, @NotNull Map<String, String> params);
}
