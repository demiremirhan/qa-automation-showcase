package io.github.demiremirhan.api.tests;

import io.github.demiremirhan.api.client.ProductClient;
import io.github.demiremirhan.api.client.SpecFactory;
import io.github.demiremirhan.api.model.Product;
import io.github.demiremirhan.api.model.ProductList;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTests {

    private final ProductClient products = new ProductClient();

    @BeforeEach
    void waitBetweenRequests() throws InterruptedException {
        Thread.sleep(500); // DummyJSON rate limit'i aşmamak için
    }

    @Test
    @DisplayName("GET /products/1 → 200, zorunlu alanlar dolu ve geçerli")
    void getSingleProduct_returnsValidFields() {
        Response response = products.getById(1);
        response.then().spec(SpecFactory.ok200());

        Product p = response.as(Product.class);

        assertThat(p.id()).isEqualTo(1);
        assertThat(p.title()).isNotBlank();
        assertThat(p.price()).isPositive();
        assertThat(p.rating()).isBetween(0.0, 5.0);
        assertThat(p.stock()).isGreaterThanOrEqualTo(0);
        assertThat(p.thumbnail()).startsWith("https://");
    }

    @ParameterizedTest(name = "id={0} → category={1}")
    @DisplayName("GET /products/{id} — data-driven kategori doğrulama")
    @CsvSource({
            "1, beauty",
            "2, beauty",
            "7, fragrances"
    })
    void getProduct_dataDriven(int id, String expectedCategory) {
        Product p = products.getById(id)
                .then().spec(SpecFactory.ok200())
                .extract().as(Product.class);

        assertThat(p.id()).isEqualTo(id);
        assertThat(p.category()).isEqualTo(expectedCategory);
        assertThat(p.title()).isNotBlank();
    }

    @Test
    @DisplayName("GET /products/999999 → 404 döner")
    void getNonExistentProduct_returns404() {
        int statusCode = products.getStatusCode(999999);
        assertThat(statusCode).isEqualTo(404);
    }

    @Test
    @DisplayName("GET /products?limit=5&skip=10 → sayfalama parametreleri korunur")
    void pagination_respectsLimitAndSkip() {
        int limit = 5;
        int skip  = 10;

        ProductList page = products.getPaged(limit, skip)
                .then().spec(SpecFactory.ok200())
                .extract().as(ProductList.class);

        assertThat(page.limit()).isEqualTo(limit);
        assertThat(page.skip()).isEqualTo(skip);
        assertThat(page.products()).hasSize(limit);
        assertThat(page.total()).isGreaterThan(limit);
    }

    @Test
    @DisplayName("GET /products/search?q=phone → eşleşen sonuçlar döner")
    void search_returnsMatchingProducts() {
        ProductList result = products.search("phone")
                .then().spec(SpecFactory.ok200())
                .extract().as(ProductList.class);

        assertThat(result.products()).isNotEmpty();
        assertThat(result.total()).isGreaterThan(0);
        assertThat(result.products())
                .allSatisfy(p -> assertThat(p.title()).isNotBlank());
    }
}