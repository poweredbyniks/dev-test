package com.musala.drones;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musala.drones.config.EnvironmentConfigContextInitializer;
import com.musala.drones.config.TestConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockserver.integration.ClientAndServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Description;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.lifecycle.Startable;

import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class, DevDronesTest.class}, properties = {
        "spring.liquibase.enabled=false",
        "eureka.client.enabled=false",
        "ribbon.eureka.enabled=false",
        "spring.main.allow-bean-definition-overriding=false"
}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DisplayName("""
        Java ENV:
        -DDOCKER_HOST=tcp://d0msfl-app01.ix.atb.su:2375
        -DDOCKER_TLS_VERIFY=0
        """
)

@ActiveProfiles("test")
@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ComponentScan(value = "su.atb.msfl.callback")
@ContextConfiguration(classes = DevDronesApp.class, initializers = EnvironmentConfigContextInitializer.class)
public class DevDronesTest {

    public static ClientAndServer mockServer;

    @Autowired
    @Qualifier("commonWebClient")
    private WebClient testWebClient;

    @Autowired
    @Qualifier("commonObjectMapper")
    private ObjectMapper mapper;

    public static StartableConnectionProvider PSQL_DB =
            new PreconfiguredPGContainer(BasePostgresContainer.getImageName(), System.getProperty("liquibasePostgre.path")); //new BasePostgresContainer();



    @BeforeClass
    public static void beforeClass() {
        mockServer = ClientAndServer.startClientAndServer(8888);
    }

    @AfterClass
    public static void afterClass() {
        mockServer.stop();
    }

    @SneakyThrows
    @Before
    public void before() {
        Thread.sleep(500);
    }

    @SneakyThrows
    @After
    public void after() {
        mockServer.reset();
        Thread.sleep(500);
    }


    //GET tests

    @Description("Positive for GET /v1/repayment/availability")
    @SneakyThrows
    @Test
    public void A_positiveCaseGetAvailability() {

    }

}