package com.musala.drones.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.codec.Decoder;
import org.springframework.core.codec.Encoder;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.MimeType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import javax.sql.DataSource;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static com.musala.drones.DevDronesTest.PSQL_DB;

@TestConfiguration
public class TestConfig {


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
    @Primary
    public ObjectMapper commonObjectMapper() {
        ObjectMapper objectMapper = (new ObjectMapper()).registerModule(new JavaTimeModule()).disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Bean({"commonExchangeDecoder"})
    public Decoder<?> commonExchangeDecoder() {
        return new Jackson2JsonDecoder(this.commonObjectMapper(), new MimeType[0]);
    }

    @Bean({"commonExchangeEncoder"})
    public Encoder<?> commonExchangeEncoder() {
        return new Jackson2JsonEncoder(this.commonObjectMapper(), new MimeType[0]);
    }

    @Bean("commonWebClient")
    @ConditionalOnProperty(name = "webclient.common.timeout.connection")
    public WebClient commonWebClient(@Value("${webclient.common.timeout.read}") Integer readTimeout,
                                     @Value("${webclient.common.timeout.write}") Integer writeTimeout,
                                     @Value("${webclient.common.timeout.connection}") Integer timeoutConnection,
                                     ExchangeStrategies exchangeStrategies) {

        HttpClient httpClient = createHttpClient("common-webclient-connection-pool", readTimeout, writeTimeout, timeoutConnection);

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .exchangeStrategies(exchangeStrategies)
                .build();
    }

    @Bean
    public ExchangeStrategies createExchangeStrategiesWithJacksonCodec(@Qualifier("commonExchangeEncoder") Encoder<?> commonExchangeEncoder,
                                                                       @Qualifier("commonExchangeDecoder") Decoder<?> commonExchangeDecoder) {
        return ExchangeStrategies.builder().codecs(clientCodecConfigurer -> {
            clientCodecConfigurer.defaultCodecs().jackson2JsonDecoder(commonExchangeDecoder);
            clientCodecConfigurer.defaultCodecs().jackson2JsonEncoder(commonExchangeEncoder);
            clientCodecConfigurer.defaultCodecs().maxInMemorySize(32 * 1024 * 1024);
        }).build();
    }

    private HttpClient createHttpClient(String connectionPoolName,
                                        Integer readTimeout,
                                        Integer writeTimeout,
                                        Integer timeoutConnection) {
        ConnectionProvider connectionProvider = ConnectionProvider
                .builder(connectionPoolName)
                .maxConnections(1000)
                .pendingAcquireMaxCount(1500)
                .pendingAcquireTimeout(Duration.ofMillis(timeoutConnection))
                .build();

        return HttpClient.create(connectionProvider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeoutConnection)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(writeTimeout, TimeUnit.MILLISECONDS));
                });
    }

}