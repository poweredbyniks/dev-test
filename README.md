# Dear visitor, welcome to Dev Drones application!

---
This manual explains how to run application and tests locally.
Before we start it is important to accept some assumptions:
- medication is always unique (combination of weight and name) and there’s no need to check if it exists in DB or not
- PSQL is used for logging storage for the purpose of simplification of the test app, in a real project it's better to use Kafka with infinite retention or Elastic

This application allows to:
- register a drone
- show all drones
- show all available for loading medications drones
- check drone battery capacity
- check drone's medication load
- load particular drone with medications

Original task could be found in `INITIALTASK.md`.

---

## Running an application

#### PRECONDITIONS:
First of all it is required to install: 
- JDK [installation guide](https://openjdk.org/install/)
- Maven [installation guide](https://maven.apache.org/install.html)
- Docker [installation guide](https://docs.docker.com/engine/install/)

After all the steps are completed it is reasonable to proceed further.

---

#### STEP 1:
Open your favorite terminal app, then clone current repository with command `git clone https://oauth:glpat-2r4Q5zpjfpstAZCfPrFy@gitlab.com/musala_soft/DEV_DRONES-ff56b8de-84b3-91b8-c7a7-acaba30b2f02.git`

#### STEP 2:
Open the folder with the project and execute command `mvn clean package`
The message `BUILD SUCCESS` will show that everything went perfect 

#### STEP 3:
Execute command `docker-compose up --build` to start application and database containers

#### STEP 4:
Using your web browser open [swagger page](http://localhost:8080/swagger-ui/index.html#/)

#### STEP 5:
Enjoy your experience

---

### Running an application using IDE
There is an option to run an application in an IDE, but in this case it required to run PostgreSQL database by your own and set parameters of your database in application.yaml file:
```
datasource:
url: ${SPRING_DATASOURCE_URL:jdbc:postgresql://db:5432/devdronesdb}
username: ${SPRING_DATASOURCE_USERNAME:postgres}
password: ${SPRING_DATASOURCE_PASSWORD:dockerpassword} 
```

---

## Running tests
For running tests use IDE (e.g. IntelliJ IDEA)

#### STEP 1:
Open com/musala/drones/DevDronesTest.java class and run tests with mandatory VM options by clicking "Edit configurations button" 
`-DliquibasePostgre.path=src/main/resources/liquibase/changelog.xml`

#### STEP 2:
Enjoy your experience