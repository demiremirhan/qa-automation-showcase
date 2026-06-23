package io.github.demiremirhan.api.client;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ProductClient {

    private static final String PRODUCTS = "/products";

    public Response getById(int id) {
        return given()
                .spec(SpecFactory.request())
                .pathParam("id", id)
                .when()
                .get(PRODUCTS + "/{id}");
    }

    public Response getAll() {
        return given()
                .spec(SpecFactory.request())
                .when()
                .get(PRODUCTS);
    }

    public Response getPaged(int limit, int skip) {
        return given()
                .spec(SpecFactory.request())
                .queryParam("limit", limit)
                .queryParam("skip",  skip)
                .when()
                .get(PRODUCTS);
    }

    public Response search(String query) {
        return given()
                .spec(SpecFactory.request())
                .queryParam("q", query)
                .when()
                .get(PRODUCTS + "/search");
    }
    public int getStatusCode(int id) {
        try {
            given()
                    .spec(SpecFactory.request())
                    .pathParam("id", id)
                    .when()
                    .get(PRODUCTS + "/{id}");
            return 200;
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg.contains("404")) return 404;
            if (msg.contains("400")) return 400;
            if (msg.contains("429")) return 429;
            throw e;
        }
    }
}