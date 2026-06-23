package io.github.demiremirhan.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Product(
        @JsonProperty("id")                  int id,
        @JsonProperty("title")               String title,
        @JsonProperty("description")         String description,
        @JsonProperty("category")            String category,
        @JsonProperty("price")               double price,
        @JsonProperty("discountPercentage")  double discountPercentage,
        @JsonProperty("rating")              double rating,
        @JsonProperty("stock")               int stock,
        @JsonProperty("brand")               String brand,
        @JsonProperty("sku")                 String sku,
        @JsonProperty("tags")                List<String> tags,
        @JsonProperty("thumbnail")           String thumbnail,
        @JsonProperty("images")              List<String> images
) {}