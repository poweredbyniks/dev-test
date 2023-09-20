package com.musala.drones.config.db;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.FileSystemResourceAccessor;
import lombok.extern.java.Log;
import org.postgresql.Driver;
import org.springframework.dao.UncategorizedDataAccessException;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.testcontainers.containers.JdbcDatabaseContainer;

import javax.annotation.Nullable;
import javax.sql.DataSource;
import java.nio.file.Paths;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Log
public class DbUtils {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private DbUtils() {
    }

    public static void initDB(DataSource ds, String liquibasePath) {
        initDB(ds, liquibasePath, new HashMap<>());
    }

    public static void initDB(DataSource ds, String liquibasePath, Map<String, Object> parameters) {

        log.info("Applying liquibase from  " + liquibasePath);

        try (Connection connection = ds.getConnection()) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Object defaultSchema = parameters.get("defaultSchema");
            if (defaultSchema != null) {
                database.setDefaultSchemaName(String.valueOf(defaultSchema));
            }


            String workDirectory = Paths.get(liquibasePath).getParent().toString();
            String changelogName = Paths.get(liquibasePath).getFileName().toString();

            Liquibase liquibase = new Liquibase(changelogName, new FileSystemResourceAccessor(workDirectory), database);

            parameters.forEach(liquibase::setChangeLogParameter);

            liquibase.update(new Contexts());

            connection.commit();
        } catch (Exception e) {
            throw new UncategorizedDataAccessException("Error in applying liquibase : " + e.getMessage(), e) {
            };
        }
    }

    public static DataSource getDbContainerDataSource(JdbcDatabaseContainer jdbcDatabaseContainer) {
        return new SimpleDriverDataSource(new Driver(), jdbcDatabaseContainer.getJdbcUrl(),
                jdbcDatabaseContainer.getUsername(), jdbcDatabaseContainer.getPassword());
    }

    public static Object blob(@Nullable String blob) {
        return blob == null ? null : new SqlLobValue(blob.getBytes());
    }

    public static String format(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        return date.format(DATE_TIME_FORMATTER);
    }

}
