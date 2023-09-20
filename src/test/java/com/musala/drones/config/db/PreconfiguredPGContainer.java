package com.musala.drones.config.db;

import com.musala.drones.config.db.BasePostgresContainer;
import com.musala.drones.config.db.DbUtils;
import junit.framework.AssertionFailedError;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.testcontainers.containers.JdbcDatabaseContainer;

import java.util.HashMap;
import java.util.Map;

@Log
public class PreconfiguredPGContainer extends BasePostgresContainer {

    public static final String LIQUIBASE_PATH_PROPERTY_NAME = "liquibasePostgre.path";
    public static final String LIQUIBASE_PATH = System.getProperty(LIQUIBASE_PATH_PROPERTY_NAME);

    private final String liquibasePath;
    private final Map<String, Object> liquibaseProperties;

    public PreconfiguredPGContainer() {
        this(getImageName(), LIQUIBASE_PATH);
    }

    public PreconfiguredPGContainer(String liquibasePath) {
        this(getImageName(), liquibasePath);
    }

    public PreconfiguredPGContainer(String dockerImageName, String liquibasePath) {
        this(dockerImageName, liquibasePath, new HashMap<String, Object>() {{
            put("defaultSchema", "public");
            put("SCHEMA", "public");
        }});
    }

    public PreconfiguredPGContainer(String dockerImageName, String liquibasePath, Map<String, Object> liquibaseProperties) {
        super(dockerImageName);
        this.liquibasePath = liquibasePath;
        this.liquibaseProperties = liquibaseProperties;

        log.info("Container from DB image: " + dockerImageName + " and applying liquibase from: " + liquibasePath + " with settings: " + liquibaseProperties);
    }

    @Override
    @SneakyThrows
    public void start() {
        super.start();

        if (StringUtils.isEmpty(liquibasePath)) {
            throw new AssertionFailedError("Liquibase path is not specified - test can not be executed.");
        }

        DbUtils.initDB(DbUtils.getDbContainerDataSource((JdbcDatabaseContainer<?>) this), liquibasePath, liquibaseProperties);
    }
}
