package io.github.demiremirhan.db.dao;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface ConnectionSupplier {
    Connection get() throws SQLException;
}