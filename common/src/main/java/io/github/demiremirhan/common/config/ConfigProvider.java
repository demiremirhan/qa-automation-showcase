package io.github.demiremirhan.common.config;

import org.aeonbits.owner.ConfigFactory;

public final class ConfigProvider {

    private static final FrameworkConfig CONFIG = ConfigFactory.create(FrameworkConfig.class);

    private ConfigProvider() {
        // utility sınıfı — örneklenemez
    }

    public static FrameworkConfig get() {
        return CONFIG;
    }
}