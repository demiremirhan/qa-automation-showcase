package io.github.demiremirhan.db.model;

public record ProductRecord(
        int id,
        String title,
        String category,
        double price,
        int stock,
        double rating,
        String sku
) {}