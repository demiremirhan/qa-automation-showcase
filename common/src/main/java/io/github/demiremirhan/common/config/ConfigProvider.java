package io.github.demiremirhan.common.config;

import org.aeonbits.owner.ConfigFactory;

/**
 * ConfigProvider: FrameworkConfig'in tek (singleton) örneğini sağlar.
 *
 * Owner'ın ConfigFactory'si, arayüzden gerçek çalışan nesneyi üretir.
 * Her yerde new'lemek yerine ConfigProvider.get() ile erişilir.
 */
public final class ConfigProvider {

    private static final FrameworkConfig CONFIG = ConfigFactory.create(FrameworkConfig.class);

    private ConfigProvider() {
        // utility sınıfı — örneklenemez
    }

    public static FrameworkConfig get() {
        return CONFIG;
    }
}