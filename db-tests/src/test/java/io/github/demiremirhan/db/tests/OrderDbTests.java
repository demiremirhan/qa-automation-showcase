package io.github.demiremirhan.db.tests;

import io.github.demiremirhan.db.dao.OrderDao;
import io.github.demiremirhan.db.model.OrderRecord;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Epic("Phase 6 — Database Testing")
@Feature("Orders Table")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderDbTests extends BaseDbTest {

    private static OrderDao dao;

    @BeforeAll
    static void setUp() throws Exception {
        loadSeedData();
        dao = new OrderDao(CONN);
    }

    // ── READ ──

    @Test
    @Order(1)
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Customer 1'in 2 siparişi olmalı")
    void findByCustomerId_shouldReturnOrders() throws SQLException {
        List<OrderRecord> orders = dao.findByCustomerId(1);
        assertThat(orders).hasSize(2);
    }

    @Test
    @Order(2)
    @DisplayName("DELIVERED statüsünde 2 sipariş olmalı")
    void findByStatus_delivered_shouldReturn2() throws SQLException {
        List<OrderRecord> orders = dao.findByStatus("DELIVERED");
        assertThat(orders).hasSize(2);
        assertThat(orders).allSatisfy(o ->
                assertThat(o.status()).isEqualTo("DELIVERED")
        );
    }

    @Test
    @Order(3)
    @DisplayName("Var olmayan müşteri → boş liste")
    void findByCustomerId_nonExistent_shouldReturnEmpty() throws SQLException {
        assertThat(dao.findByCustomerId(9999)).isEmpty();
    }

    // ── CREATE ──

    @Test
    @Order(10)
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Yeni sipariş insert → ID döner")
    void insert_shouldReturnId() throws SQLException {
        int id = dao.insert(2, 1, 5, 49.95, "PENDING");
        assertThat(id).isPositive();

        List<OrderRecord> orders = dao.findByCustomerId(2);
        assertThat(orders).anyMatch(o -> o.id() == id && o.quantity() == 5);
    }

    @Test
    @Order(11)
    @DisplayName("Geçersiz customer_id → FK constraint ihlali")
    void insert_invalidCustomer_shouldThrow() {
        assertThatThrownBy(() ->
                dao.insert(9999, 1, 1, 9.99, "PENDING")
        ).isInstanceOf(SQLException.class);
    }

    @Test
    @Order(12)
    @DisplayName("Geçersiz status → check constraint ihlali")
    void insert_invalidStatus_shouldThrow() {
        assertThatThrownBy(() ->
                dao.insert(1, 1, 1, 9.99, "INVALID_STATUS")
        ).isInstanceOf(SQLException.class);
    }

    // ── UPDATE ──

    @Test
    @Order(20)
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Status PENDING → CONFIRMED geçişi")
    void updateStatus_shouldPersist() throws SQLException {
        List<OrderRecord> pending = dao.findByStatus("PENDING");
        assertThat(pending).isNotEmpty();

        int orderId = pending.get(0).id();
        boolean updated = dao.updateStatus(orderId, "CONFIRMED");
        assertThat(updated).isTrue();

        List<OrderRecord> orders = dao.findByCustomerId(pending.get(0).customerId());
        assertThat(orders).anyMatch(o -> o.id() == orderId && o.status().equals("CONFIRMED"));
    }

    // ── AGGREGATION ──

    @Test
    @Order(30)
    @DisplayName("DELIVERED siparişlerin toplam geliri hesaplanmalı")
    void totalRevenueByStatus_delivered() throws SQLException {
        double revenue = dao.totalRevenueByStatus("DELIVERED");
        assertThat(revenue).isGreaterThan(0);
    }

    @Test
    @Order(31)
    @DisplayName("Var olmayan status → gelir 0 olmalı")
    void totalRevenueByStatus_nonExistent_shouldBeZero() throws SQLException {
        double revenue = dao.totalRevenueByStatus("NONEXISTENT");
        assertThat(revenue).isEqualTo(0);
    }

    // ── JOIN ──

    @Test
    @Order(40)
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("JOIN query — müşteri 1 sipariş özetleri")
    void orderSummaries_shouldContainProductAndCustomerInfo() throws SQLException {
        List<String> summaries = dao.orderSummariesByCustomer(1);

        assertThat(summaries).isNotEmpty();
        assertThat(summaries).allSatisfy(s -> {
            assertThat(s).contains("|");
            assertThat(s).contains("qty:");
        });
    }
}