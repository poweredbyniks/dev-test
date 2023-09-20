package com.musala.drones.config.db;

import javax.sql.DataSource;

/**
 * API db connection
 */
public interface DbConnectionProvider {
    String jdbcUrl();

    String userName();

    String password();

    DataSource dataSource();
}
