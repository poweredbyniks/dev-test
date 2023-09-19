package com.musala.drones.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@ConditionalOnProperty(name = "spring.liquibase.enabled", havingValue = "true")
public class LiquibaseRunner {

    @Value("${spring.datasource.hikari.schema:public}")
    private String schemaName;

    @Value("${spring.datasource.username:test}")
    private String dbUserName;

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:/liquibase/changelog.xml");
        liquibase.setDataSource(dataSource);
        var parametersMap = new HashMap<String, String>();
        parametersMap.put("SCHEMA", schemaName);
        parametersMap.put("DB_USER", dbUserName);
        liquibase.setChangeLogParameters(parametersMap);
        return liquibase;
    }
}
