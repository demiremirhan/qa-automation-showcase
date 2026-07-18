package io.github.demiremirhan.db.tests;

import io.github.demiremirhan.common.config.ConfigProvider;
import io.github.demiremirhan.db.dao.ConnectionSupplier;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Testcontainers
abstract class BaseDbTest {

    @Container
    static final PostgreSQLContainer<?> PG = new PostgreSQLContainer<>(
            ConfigProvider.get().postgresImage()
    )
            .withDatabaseName(ConfigProvider.get().databaseName())
            .withUsername(ConfigProvider.get().databaseUsername())
            .withPassword(ConfigProvider.get().databasePassword())
            .withInitScript("init-schema.sql");

    static final ConnectionSupplier CONN = () ->
            DriverManager.getConnection(PG.getJdbcUrl(), PG.getUsername(), PG.getPassword());

    static void loadSeedData() throws SQLException, IOException {
        String sql = readClasspathFile("seed-data.sql");
        try (Connection conn = CONN.get();
             Statement st = conn.createStatement()) {
            st.execute(sql);
        }
    }

    static void truncateTable(String tableName) throws SQLException {
        try (Connection conn = CONN.get();
             Statement st = conn.createStatement()) {
            st.execute("TRUNCATE TABLE " + tableName + " RESTART IDENTITY CASCADE");
        }
    }

    private static String readClasspathFile(String path) throws IOException {
        try (InputStream is = BaseDbTest.class.getClassLoader().getResourceAsStream(path)) {
            if (is == null) throw new IOException("Classpath resource bulunamadı: " + path);
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}