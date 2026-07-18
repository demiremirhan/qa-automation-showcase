package io.github.demiremirhan.common.config;

import org.aeonbits.owner.Config;

@Config.Sources({
        "system:properties",
        "classpath:config-${env}.properties",
        "classpath:config.properties"
})
public interface FrameworkConfig extends Config {

    @Key("api.base.uri")
    @DefaultValue("https://dummyjson.com")
    String apiBaseUri();

    @Key("api.timeout.ms")
    @DefaultValue("10000")
    int apiTimeoutMs();

    @Key("env")
    @DefaultValue("prod")
    String environment();

    @Key("ui.base.url")
    @DefaultValue("https://www.saucedemo.com")
    String uiBaseUrl();

    @Key("selenium.grid.url")
    @DefaultValue("http://localhost:4444/wd/hub")
    String seleniumGridUrl();

    @Key("testcontainers.postgres.image")
    @DefaultValue("postgres:16-alpine")
    String postgresImage();

    @Key("db.database.name")
    @DefaultValue("showcase_test")
    String databaseName();

    @Key("db.username")
    @DefaultValue("testuser")
    String databaseUsername();

    @Key("db.password")
    @DefaultValue("testpass")
    String databasePassword();
}