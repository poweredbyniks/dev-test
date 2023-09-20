package com.musala.drones.config.db;

import org.testcontainers.lifecycle.Startable;

/**
 * API подключения к ДБ в контейнере
 */
public interface StartableConnectionProvider extends DbConnectionProvider, Startable {
}
