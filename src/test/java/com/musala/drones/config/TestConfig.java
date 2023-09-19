package com.musala.drones.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import static com.musala.drones.DevDronesTest.PSQL_DB;
import static su.atb.msfl.MsflRepaymentTest.ATB_DBOFL_DB;

@TestConfiguration
public class TestConfig {

    @Value("${rabbitmq.get-queue}")
    String queueName;

    @Value("${rabbitmq.exchange}")
    String exchange;

    @Bean("testJdbcTemplateDboFlDB")
    public JdbcTemplate testJdbcTemplatePreconfiguredPgDb(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean("testDataSource")
    @Primary
    public DataSource testDataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl(PSQL_DB.jdbcUrl());
        config.setUsername(PSQL_DB.userName());
        config.setPassword(PSQL_DB.password());
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        return new HikariDataSource(config);
    }


    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public MessageConverter jsonMessageConverter(@Qualifier("commonObjectMapper") ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    @Primary
    public ConnectionFactory testRabbitConnectionFactory(@Qualifier("commonConnectionFactory") ConnectionFactory connectionFactory) {
        return connectionFactory;
    }
}