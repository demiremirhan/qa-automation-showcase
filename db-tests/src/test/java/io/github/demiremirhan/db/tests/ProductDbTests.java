package io.github.demiremirhan.db.tests;

import io.github.demiremirhan.db.dao.ProductDao;
import io.github.demiremirhan.db.model.ProductRecord;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Epic("Phase 6 — Database Testing")
@Feature("Products Table")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductDbTests extends BaseDbTest {

    private static ProductDao dao;

    @BeforeAll
    static void setUp() throws Exception {
        loadSeedData();
        dao = new ProductDao(CONN);
    }

    // ── READ ──

    @Test
    @Order(1)
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Seed data yüklendi — products tablosunda 10 kayıt olmalı")
    void seedData_shouldHave10Products() throws SQLException {
        List<ProductRecord> all = dao.findAll();
        assertThat(all).hasSize(10);
    }

    @Test
    @Order(2)
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("findById(1) → 'Essence Mascara', beauty, 9.99")
    void findById_shouldReturnCorrectProduct() throws SQLException {
        Optional<ProductRecord> opt = dao.findById(1);

        assertThat(opt).isPresent();
        ProductRecord p = opt.get();
        assertThat(p.title()).isEqualTo("Essence Mascara");
        assertThat(p.category()).isEqualTo("beauty");
        assertThat(p.price()).isEqualTo(9.99);
        assertThat(p.sku()).isEqualTo("RCH-001");
    }

    @Test
    @Order(3)
    @DisplayName("findById(9999) → empty — var olmayan ID")
    void findById_nonExistent_shouldReturnEmpty() throws SQLException {
        assertThat(dao.findById(9999)).isEmpty();
    }

    @Test
    @Order(4)
    @DisplayName("findByCategory('beauty') → 3 ürün")
    void findByCategory_beauty_shouldReturn3() throws SQLException {
        List<ProductRecord> beautyProducts = dao.findByCategory("beauty");

        assertThat(beautyProducts).hasSize(3);
        assertThat(beautyProducts).allSatisfy(p ->
                assertThat(p.category()).isEqualTo("beauty")
        );
    }

    @Test
    @Order(5)
    @DisplayName("findByCategory('nonexistent') → boş liste")
    void findByCategory_unknown_shouldReturnEmpty() throws SQLException {
        assertThat(dao.findByCategory("nonexistent")).isEmpty();
    }

    // ── CREATE ──

    @Test
    @Order(10)
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Yeni ürün insert → ID döner ve geri okunabilir")
    void insert_shouldReturnIdAndBeReadable() throws SQLException {
        int newId = dao.insert("Test Widget", "gadgets", 29.99, 50, 4.20, "TST-001");
        assertThat(newId).isPositive();

        Optional<ProductRecord> opt = dao.findById(newId);
        assertThat(opt).isPresent();

        ProductRecord created = opt.get();
        assertThat(created.title()).isEqualTo("Test Widget");
        assertThat(created.category()).isEqualTo("gadgets");
        assertThat(created.price()).isEqualTo(29.99);
        assertThat(created.stock()).isEqualTo(50);
    }

    @Test
    @Order(11)
    @DisplayName("Duplicate SKU insert → unique constraint ihlali")
    void insert_duplicateSku_shouldThrow() {
        assertThatThrownBy(() ->
                dao.insert("Duplicate Product", "beauty", 5.00, 10, 3.00, "RCH-001")
        ).isInstanceOf(SQLException.class);
    }

    @Test
    @Order(12)
    @DisplayName("Negatif fiyat insert → check constraint ihlali")
    void insert_negativePrice_shouldThrow() {
        assertThatThrownBy(() ->
                dao.insert("Bad Product", "beauty", -5.00, 10, 3.00, "NEG-001")
        ).isInstanceOf(SQLException.class);
    }

    // ── UPDATE ──

    @Test
    @Order(20)
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Stock güncelleme → yeni değer doğru okunmalı")
    void updateStock_shouldPersistNewValue() throws SQLException {
        boolean updated = dao.updateStock(1, 999);
        assertThat(updated).isTrue();

        Optional<ProductRecord> opt = dao.findById(1);
        assertThat(opt).isPresent();
        assertThat(opt.get().stock()).isEqualTo(999);
    }

    @Test
    @Order(21)
    @DisplayName("Var olmayan ürün stock güncellemesi → false döner")
    void updateStock_nonExistent_shouldReturnFalse() throws SQLException {
        assertThat(dao.updateStock(9999, 10)).isFalse();
    }

    // ── DELETE ──

    @Test
    @Order(30)
    @DisplayName("Ürün silme → findById artık empty döner")
    void deleteById_shouldRemoveRecord() throws SQLException {
        int id = dao.insert("To Be Deleted", "temp", 1.00, 1, 1.00, "DEL-001");
        assertThat(dao.findById(id)).isPresent();

        boolean deleted = dao.deleteById(id);
        assertThat(deleted).isTrue();
        assertThat(dao.findById(id)).isEmpty();
    }

    // ── AGGREGATION ──

    @Test
    @Order(40)
    @DisplayName("countByCategory('fragrances') → 2")
    void countByCategory_shouldReturnCorrectCount() throws SQLException {
        long count = dao.countByCategory("fragrances");
        assertThat(count).isEqualTo(2);
    }

    @Test
    @Order(41)
    @DisplayName("avgPriceByCategory('laptops') → 999.99")
    void averagePrice_laptops_shouldBeCalculated() throws SQLException {
        Optional<Double> avg = dao.averagePriceByCategory("laptops");
        assertThat(avg).isPresent();
        assertThat(avg.get()).isCloseTo(999.99, Offset.offset(0.01));
    }

    @Test
    @Order(42)
    @DisplayName("avgPriceByCategory('nonexistent') → empty")
    void averagePrice_unknown_shouldReturnEmpty() throws SQLException {
        Optional<Double> avg = dao.averagePriceByCategory("nonexistent");
        assertThat(avg).isEmpty();
    }
}