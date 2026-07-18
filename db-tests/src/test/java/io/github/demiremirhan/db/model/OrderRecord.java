package io.github.demiremirhan.db.model;

public record OrderRecord(
        int id,
        int customerId,
        int productId,
        int quantity,
        double totalPrice,
        String status
) {}