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
    public int getStatusCode(int pId) {
        try {
            return given()
                    .spec(SpecFactory.request())
                    .pathParam("id", pId)
                    .when()
                    .get(PRODUCTS + "/{id}")
                    .getStatusCode();
        } catch (Exception e) {
            //noinspection ConstantValue
            if (e instanceof io.restassured.internal.http.HttpResponseException hre) {
                return hre.getStatusCode();
            }
            throw new RuntimeException(e);
        }
    }
}