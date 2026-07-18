package io.github.demiremirhan.db.dao;

import io.github.demiremirhan.db.model.OrderRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {

    private final ConnectionSupplier connectionSupplier;

    public OrderDao(ConnectionSupplier connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
    }

    public int insert(int customerId, int productId, int quantity, double totalPrice, String status)
            throws SQLException {
        String sql = """
                INSERT INTO orders (customer_id, product_id, quantity, total_price, status)
                VALUES (?, ?, ?, ?, ?)
                RETURNING id
                """;
        try (Connection conn = connectionSupplier.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.setDouble(4, totalPrice);
            ps.setString(5, status);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    public List<OrderRecord> findByCustomerId(int customerId) throws SQLException {
        String sql = """
                SELECT id, customer_id, product_id, quantity, total_price, status
                FROM orders
                WHERE customer_id = ?
                ORDER BY id
                """;
        List<OrderRecord> result = new ArrayList<>();
        try (Connection conn = connectionSupplier.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        }
        return result;
    }

    public List<OrderRecord> findByStatus(String status) throws SQLException {
        String sql = """
                SELECT id, customer_id, product_id, quantity, total_price, status
                FROM orders
                WHERE status = ?
                ORDER BY id
                """;
        List<OrderRecord> result = new ArrayList<>();
        try (Connection conn = connectionSupplier.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        }
        return result;
    }

    public boolean updateStatus(int orderId, String newStatus) throws SQLException {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = connectionSupplier.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, orderId);
            return ps.executeUpdate() == 1;
        }
    }

    public double totalRevenueByStatus(String status) throws SQLException {
        String sql = "SELECT COALESCE(SUM(total_price), 0) FROM orders WHERE status = ?";
        try (Connection conn = connectionSupplier.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getDouble(1);
            }
        }
    }

    public List<String> orderSummariesByCustomer(int customerId) throws SQLException {
        String sql = """
                SELECT c.first_name, c.last_name, p.title, o.quantity, o.status
                FROM orders o
                JOIN customers c ON o.customer_id = c.id
                JOIN products  p ON o.product_id  = p.id
                WHERE o.customer_id = ?
                ORDER BY o.id
                """;
        List<String> summaries = new ArrayList<>();
        try (Connection conn = connectionSupplier.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    summaries.add("%s %s | %s | qty:%d | %s".formatted(
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("title"),
                            rs.getInt("quantity"),
                            rs.getString("status")
                    ));
                }
            }
        }
        return summaries;
    }

    private OrderRecord mapRow(ResultSet rs) throws SQLException {
        return new OrderRecord(
                rs.getInt("id"),
                rs.getInt("customer_id"),
                rs.getInt("product_id"),
                rs.getInt("quantity"),
                rs.getDouble("total_price"),
                rs.getString("status")
        );
    }
}