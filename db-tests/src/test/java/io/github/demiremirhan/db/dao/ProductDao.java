package io.github.demiremirhan.db.dao;

import io.github.demiremirhan.db.model.ProductRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDao {

    private final ConnectionSupplier connectionSupplier;

    public ProductDao(ConnectionSupplier connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
    }

    public Optional<ProductRecord> findById(int id) throws SQLException {
        String sql = "SELECT id, title, category, price, stock, rating, sku FROM products WHERE id = ?";
        try (Connection conn = connectionSupplier.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        }
    }

    public List<ProductRecord> findByCategory(String category) throws SQLException {
        String sql = "SELECT id, title, category, price, stock, rating, sku FROM products WHERE category = ? ORDER BY id";
        List<ProductRecord> result = new ArrayList<>();
        try (Connection conn = connectionSupplier.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        }
        return result;
    }

    public List<ProductRecord> findAll() throws SQLException {
        String sql = "SELECT id, title, category, price, stock, rating, sku FROM products ORDER BY id";
        List<ProductRecord> result = new ArrayList<>();
        try (Connection conn = connectionSupplier.get();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(mapRow(rs));
            }
        }
        return result;
    }

    public int insert(String title, String category, double price, int stock, double rating, String sku)
            throws SQLException {
        String sql = "INSERT INTO products (title, category, price, stock, rating, sku) VALUES (?,?,?,?,?,?) RETURNING id";
        try (Connection conn = connectionSupplier.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, category);
            ps.setDouble(3, price);
            ps.setInt(4, stock);
            ps.setDouble(5, rating);
            ps.setString(6, sku);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    public boolean updateStock(int productId, int newStock) throws SQLException {
        String sql = "UPDATE products SET stock = ? WHERE id = ?";
        try (Connection conn = connectionSupplier.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newStock);
            ps.setInt(2, productId);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean deleteById(int id) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = connectionSupplier.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    public long countByCategory(String category) throws SQLException {
        String sql = "SELECT COUNT(*) FROM products WHERE category = ?";
        try (Connection conn = connectionSupplier.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        }
    }

    public Optional<Double> averagePriceByCategory(String category) throws SQLException {
        String sql = "SELECT AVG(price) FROM products WHERE category = ?";
        try (Connection conn = connectionSupplier.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                double avg = rs.getDouble(1);
                return rs.wasNull() ? Optional.empty() : Optional.of(avg);
            }
        }
    }

    private ProductRecord mapRow(ResultSet rs) throws SQLException {
        return new ProductRecord(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("category"),
                rs.getDouble("price"),
                rs.getInt("stock"),
                rs.getDouble("rating"),
                rs.getString("sku")
        );
    }
}