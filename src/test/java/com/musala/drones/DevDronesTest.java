package com.musala.drones;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musala.drones.config.InitTestDataService;
import com.musala.drones.config.db.BasePostgresContainer;
import com.musala.drones.config.EnvironmentConfigContextInitializer;
import com.musala.drones.config.db.PreconfiguredPGContainer;
import com.musala.drones.config.TestConfig;
import com.musala.drones.config.db.StartableConnectionProvider;
import com.musala.drones.dto.DroneBatteryCapacityDto;
import com.musala.drones.dto.DroneDto;
import com.musala.drones.model.Model;
import com.musala.drones.model.State;
import com.musala.drones.repository.DroneRepository;
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
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.lifecycle.Startable;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class, DevDronesTest.class}, properties = {
        "spring.liquibase.enabled=false",
        "eureka.client.enabled=false",
        "ribbon.eureka.enabled=false",
        "spring.main.allow-bean-definition-overriding=false"
}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ContextConfiguration(classes = DevDronesApp.class, initializers = EnvironmentConfigContextInitializer.class)
@DisplayName("Integration test for module: dbofl-operations" +
        "Java ENV: -DliquibasePostgre.path=src/main/resources/liquibase/changelog.xml")
public class DevDronesTest {

    public static ClientAndServer mockServer;

    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private InitTestDataService initTestDataService;

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
        Stream.of(PSQL_DB).parallel().forEach(Startable::start);
    }

    @AfterClass
    public static void afterClass() {
        Stream.of(PSQL_DB).parallel().forEach(Startable::stop);
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
        droneRepository.deleteAll();
        mockServer.reset();
    }


    //GET tests

    @Description("Positive")
    @SneakyThrows
    @Test
    public void A_positiveCaseGetAvailableDrones() {
        initDataForPositiveCaseAvailableDrones();
        DroneDto[] response = testWebClient.mutate().baseUrl("http://localhost:8080").build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/available-drones")
                        .build())
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(DroneDto[].class))
                .block();


        assertThat(response).isNotNull();
        assertThat(response.length).isEqualTo(1);
        for (DroneDto droneDto : response) {
            assertThat(droneDto.getBatteryCapacity() >= 25);
            assertThat(droneDto.getState()).isEqualTo(State.IDLE);
        }
    }

    @Description("Positive")
    @SneakyThrows
    @Test
    public void B_positiveCaseGetDroneBatteryCapacity() {
        initDataForGetDroneBatteryCapacity();
        DroneBatteryCapacityDto response = testWebClient.mutate().baseUrl("http://localhost:8080").build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/drone/{serialNumber}/capacity")
                        .build("C-3PO"))
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(DroneBatteryCapacityDto.class))
                .block();

        assertThat(response).isNotNull();
        assertThat(response.getBatteryCapacity()).isEqualTo(42);
        assertThat(response.getSerialNumber()).isEqualTo("C-3PO");
    }

    @Description("Positive")
    @SneakyThrows
    @Test
    public void Ba_negativeCaseGetDroneBatteryCapacityWithAbsentDrone() {
        initDataForGetDroneBatteryCapacity();
        DroneBatteryCapacityDto response = testWebClient.mutate().baseUrl("http://localhost:8080").build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/drone/{serialNumber}/capacity")
                        .build("R2D2"))
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(DroneBatteryCapacityDto.class))
                .block();
        assertThat(response).isNotNull();
    }

    @Description("Positive")
    @Test
    public void C_positiveCasePostDrone() {
        testWebClient.mutate().baseUrl("http://localhost:8080").build()
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/drones")
                        .build("R2D2"))
                .body(BodyInserters.fromValue(buildPositiveDroneDto()))
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(Void.class))
                .block();
        await().until(() -> droneRepository.findAll().size() == 1);
    }

    @Description("Positive")
    @SneakyThrows
    @Test
    public void Ca_NegativeCasePostDroneWithExistingSerialNumber() {
        testWebClient.mutate().baseUrl("http://localhost:8080").build()
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/drones")
                        .build())
                .body(BodyInserters.fromValue(buildNegativeDroneDto()))
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(Void.class))
                .block();
    }

    @Description("Positive")
    @SneakyThrows
    @Test
    public void Cb_NegativeCasePostDroneWithExistingSerialNumber() {
        testWebClient.mutate().baseUrl("http://localhost:8080").build()
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/drones")
                        .build())
                .body(BodyInserters.fromValue(buildNegativeDroneDto()))
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(Void.class))
                .block();
    }

    @Description("Positive")
    @SneakyThrows
    @Test
    public void Cc_NegativeCasePostDroneWithBatteryTypo() {
        testWebClient.mutate().baseUrl("http://localhost:8080").build()
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/drones")
                        .build())
                .body(BodyInserters.fromValue(buildNegativeWeightExceedDroneDto()))
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(Void.class))
                .block();
    }

    @Description("Positive")
    @SneakyThrows
    @Test
    public void D_positiveCaseGetDroneMedications() {
        initDataForGetDroneBatteryCapacity();
        DroneBatteryCapacityDto response = testWebClient.mutate().baseUrl("http://localhost:8080").build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/drone/{serialNumber}/capacity")
                        .build("C-3PO"))
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(DroneBatteryCapacityDto.class))
                .block();

        assertThat(response).isNotNull();
        assertThat(response.getBatteryCapacity()).isEqualTo(42);
        assertThat(response.getSerialNumber()).isEqualTo("C-3PO");
    }

    private DroneDto buildPositiveDroneDto() {
        final DroneDto droneDto = new DroneDto();
        droneDto.setBatteryCapacity(100);
        droneDto.setSerialNumber(UUID.randomUUID().toString());
        droneDto.setWeightLimit(500);
        droneDto.setModel(Model.Cruiserweight);
        droneDto.setState(State.IDLE);
        return droneDto;
    }

    private DroneDto buildNegativeDroneDto() {
        final DroneDto droneDto = new DroneDto();
        droneDto.setBatteryCapacity(100);
        droneDto.setSerialNumber("C-3PO");
        droneDto.setWeightLimit(500);
        droneDto.setModel(Model.Cruiserweight);
        droneDto.setState(State.IDLE);
        return droneDto;
    }

    private DroneDto buildNegativeWeightExceedDroneDto() {
        final DroneDto droneDto = new DroneDto();
        droneDto.setBatteryCapacity(110);
        droneDto.setSerialNumber("R2D2");
        droneDto.setWeightLimit(500);
        droneDto.setModel(Model.Cruiserweight);
        droneDto.setState(State.IDLE);
        return droneDto;
    }

    private void initDataForPositiveCaseAvailableDrones() {
        initTestDataService.initDataForPositiveCaseAvailableDrones();
    }

    private void initDataForGetDroneBatteryCapacity() {
        initTestDataService.initDataForPositiveCaseGetDroneBatteryCapacity();
    }

    private void initDataForNegativePostDroneWithExistingSerialNumber() {
        initTestDataService.initDataForNegativePostDroneWithExistingSerialNumber();
    }

}