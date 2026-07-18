package io.github.demiremirhan.db.model;

public record CustomerRecord(
        int id,
        String firstName,
        String lastName,
        String email,
        String city,
        boolean active
) {}