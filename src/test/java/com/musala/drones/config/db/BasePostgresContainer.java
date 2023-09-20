package com.musala.drones.config.db;

import com.google.common.primitives.Ints;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.rnorth.ducttape.unreliables.Unreliables.retryUntilSuccess;


public class BasePostgresContainer extends PostgreSQLContainer implements StartableConnectionProvider {

    public static final String POSTGRESQL_DOCKER_IMAGE_PROPERTY = "postgresql.image";
    public static final String POSTGRESQL_IMAGE = System.getProperty(POSTGRESQL_DOCKER_IMAGE_PROPERTY);
    protected static final String WAIT_STRATEGY_VIA_SELECT = System.getProperty("postgres.startup.wait.strategy", "SQL");
    protected static final int STARTUP_TIMEOUT_SECONDS = Optional.ofNullable(
                    Ints.tryParse(System.getProperty("postgres.startup.timeout.seconds", "")))
            .orElse(90);

    protected static final int ATTEMPTS_COUNT = Optional.ofNullable(
            Ints.tryParse(System.getProperty("postgres.startup.retry.count", ""))).orElse(1);


    public BasePostgresContainer() {
        this(getImageName());
    }

    public BasePostgresContainer(String dockerImageName) {
        super(dockerImageName);

        JdbcDatabaseContainer<?> self = this;

        if ("SQL".equals(WAIT_STRATEGY_VIA_SELECT)) {
            this.waitStrategy = new org.testcontainers.containers.wait.strategy.AbstractWaitStrategy() {
                @Override
                protected void waitUntilReady() {
                    retryUntilSuccess(STARTUP_TIMEOUT_SECONDS, TimeUnit.SECONDS,
                            () -> new JdbcTemplate(DbUtils.getDbContainerDataSource(self)).queryForObject("select 1", String.class));
                }
            };
        }

        withStartupAttempts(ATTEMPTS_COUNT);
    }

    public static String getImageName() {
        return StringUtils.isEmpty(POSTGRESQL_IMAGE) ? "postgres:11.9" : POSTGRESQL_IMAGE;
    }

    @Override
    @SneakyThrows
    public void start() {
        super.start();
    }

    @Override
    public String jdbcUrl() {
        return getJdbcUrl();
    }

    @Override
    public String userName() {
        return getUsername();
    }

    @Override
    public String password() {
        return getPassword();
    }

    @Override
    public DataSource dataSource() {
        return DbUtils.getDbContainerDataSource((PostgreSQLContainer) this);
    }
}
