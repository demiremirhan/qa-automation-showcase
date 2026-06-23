package io.github.demiremirhan.api.client;

import io.github.demiremirhan.common.config.ConfigProvider;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static org.hamcrest.Matchers.lessThan;

public final class SpecFactory {

    private SpecFactory() {}

    /**
     * Normal spec — 4xx/5xx'te exception fırlatır (pozitif testler için).
     */
    public static RequestSpecification request() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigProvider.get().apiBaseUri())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    /**
     * Negatif testler için spec — 4xx/5xx response'u exception değil
     * Response nesnesi olarak döndürür.
     */
    public static RequestSpecification requestAllowingErrors() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigProvider.get().apiBaseUri())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setConfig(RestAssuredConfig.newConfig()
                        .httpClient(HttpClientConfig.httpClientConfig()
                                .setParam("http.protocol.allow-circular-redirects", false)))
                .build();
    }

    public static ResponseSpecification ok200() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .expectResponseTime(lessThan((long) ConfigProvider.get().apiTimeoutMs()))
                .build();
    }
}