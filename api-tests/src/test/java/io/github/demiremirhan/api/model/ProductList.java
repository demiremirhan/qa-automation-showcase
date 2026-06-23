package io.github.demiremirhan.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProductList(
        @JsonProperty("products") List<Product> products,
        @JsonProperty("total")    int total,
        @JsonProperty("skip")     int skip,
        @JsonProperty("limit")    int limit
) {}