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
}