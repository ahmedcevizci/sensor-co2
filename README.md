# Introduction
Carbon Dioxide (CO2) is all around us and we are constantly expelling it, but in high concentration CO2 can be harmful or even lethal.CO2 Levels between 2000 and 5000 ppm are associated with headaches, sleepiness poor concentration, loss of attention, increased heart rate and slight nausea may also be present.
# Acceptance criteria
- The service should be able to receive measurements from each sensor at the rate of 1 per minute
- If the CO2 level exceeds 2000 ppm the sensor status should be set WARN
- If the service receives 3 or more consecutive measurements higher than 2000 the sensor status should be set to ALERT
- When the sensor reaches to status ALERT an alert should be stored
- If the service receives 3 or more consecutive measurements lower than 2000 the sensor status should be set to OK
- The service should provide the following metrics about each sensor
    - Average CO2 level for the last 30 days
    - Maximum CO2 Level in the last 30 days
• It is possible to list all the alerts for a given sensor
# API
- Collect sensor mesurements
```
POST /api/v1/sensors/{uuid}/mesurements
{
"co2" : 2000,
"time" : "2019-02-01T18:55:47+00:00"
}
```
- Sensor status
```
GET /api/v1/sensors/{uuid}
Response:
{
"status" : "OK" // Possible status OK,WARN,ALERT
}
```
- Sensor metrics
```
GET /api/v1/sensors/{uuid}/metrics
Response:
{
"maxLast30Days" : 1200,
"avgLast30Days" : 900
}
```
- Listing alerts
```
GET /api/v1/sensors/{uuid}/alerts
Response:
[
    {
    "startTime" : "2019-02-02T18:55:47+00:00",
    "endTime" : "2019-02-02T20:00:47+00:00",
    "mesurement1" : 2100,
    "mesurement2" : 2200,
    "mesurement3" : 2100,
    }
]
```


# CO2 Sensor API
The CO2-Sensor-API is an API that enables TODO

## Prerequisites
What things you need to install the software and how to install them?

This project requires you to pre-install the following tools:

- Apache Maven 3.5.2
- Java JDK 1.8
- Docker and Docker Compose

## Runtime Dependencies
* Postgres

    
## Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Compiling & Installing Project
    
```
mvn install
```

### Running the Tests Locally
Since the application uses Maven, running test is as easy as running one line command.

#### Unit tests
The unit tests in this project will test the consumer, producer and the implementation of the business layer (service). To run these tests you can run the following command

```
mvn test
```

#### End to end (E2E) tests
The E2E tests in this project will test whole API endpoints to ensure quality criteria are met defined by API design. To run these tests you can run the following command

```
mvn verify
```

### Running Project Locally
For running it locally, run following command in directory where `docker-compose.yml` file is located in order to provide runtime dependencies required for CO2 Sensor API.

``` 
docker-compose up
```

##### Database
run ```docker-compose up -d```

This will start you an instance of PostgreSQL Database


##### Run Spring Application
After running Docker, run the spring boot application by the following command 

```
mvn spring-boot:run
```

##### Generating API Documentation (Swagger)
After running CO2 Sensor API, a swagger file is locally available at ```{{url}}/swagger-ui.html``` and should be fully usable as a rest client.

Remotely you can access the swagger json on ```{{url}}/v2/api-docs?group=CO2-Sensor-API-V2```

This is also the required documentation for this API.

Logging is implemented in json format.




