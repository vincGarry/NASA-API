# NASA API

### This API and services are  used to retrieve and inspect near earth objects (NEO) data from NASA Open API

This application requires:
- OpenJDK 17 or up
- API Key from  NASA Open API (https://api.nasa.gov/)


How to install:

- Install the requirements by doing `maven clean install`
- Put the API key inside the application.properties
- Run the application
- You can put `spring.h2.console.enabled=true` inside application.properties if you want to use the webservice for H2 Database and access it through [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- If you want to create new Database file with new username and password you can change this part in application.properties
```
spring.datasource.url=jdbc:h2:file:./src/main/resources/data/NASA
spring.datasource.username=sa
spring.datasource.password=password
spring.h2.console.enabled=true 
```
Make sure to enable the H2 console in properties, and then access the H2 console to run this DDL
```
CREATE TABLE  IF NOT EXISTS tb_asteroid(
    tba_id INT PRIMARY KEY,
    tba_name VARCHAR(255),
    tba_diameter_min NUMERIC,
    tba_diameter_max NUMERIC,
    tba_is_hazardous BOOLEAN,
    tba_abs_magnitude NUMERIC
);

CREATE TABLE  IF NOT EXISTS tb_close_approach_data(
    tbcad_id VARCHAR(255) NOT NULL PRIMARY KEY,
    tbcad_asteroid_id VARCHAR(255),
    tbcad_close_approach_date DATE,
    tbcad_relative_velocity NUMERIC,
    tbcad_miss_distance NUMERIC
    );
```

## Endpoint Documentation

### `/getTenClosestAsteroids`
This **POST** endpoint will fetch ten closest asteroids data from NASA Open API by specific dates

Request body(application/json):
```
{
  "startDate": "2024-01-01",
  "endDate": "2024-01-02"
}
```
Response:
```

  {
    "name": "string",
    "id": "string",
    "diameterMin": 0,
    "diameterMax": 0,
    "isPotentiallyHazardousAsteroid": true,
    "absoluteMagnitudeH": 0,
    "closeApproachData": [
      {
        "closeApproachDate": "string",
        "relativeVelocity": 0,
        "missDistance": 0
      }
    ]
  }
]
```

### `/getTenClosestAsteroidsByDistance`
This **POST** endpoint will fetch ten closest asteroids from NASA Open API by specific dates and minimum distance limit in kilometers

Request body(application/json):
```
{
  "startDate": "string",
  "endDate": "string",
  "distance": "string"
}
```
Response:
```

  {
    "name": "string",
    "id": "string",
    "diameterMin": 0,
    "diameterMax": 0,
    "isPotentiallyHazardousAsteroid": true,
    "absoluteMagnitudeH": 0,
    "closeApproachData": [
      {
        "closeApproachDate": "string",
        "relativeVelocity": 0,
        "missDistance": 0
      }
    ]
  }
]
```
### `/getSpecificAsteroidById`
This **POST** endpoint will fetch one specific asteroid data from NASA Open API by using NASA JPL small body ID (SPK-ID).

Request body(application/json): `"stringId"`

Response: 
```
{
  "name": "string",
  "id": "string",
  "diameterMin": 0,
  "diameterMax": 0,
  "isPotentiallyHazardousAsteroid": true,
  "absoluteMagnitudeH": 0,
  "closeApproachData": [
    {
      "closeApproachDate": "string",
      "relativeVelocity": 0,
      "missDistance": 0
    }
  ]
}
```
### `/getTenClosestAsteroidAndSaveToDB`
This **POST** endpoint will fetch ten closest asteroids data from NASA Open API by specific dates while also saving it into the database

Request body(application/json):
```
{
  "startDate": "2024-01-01",
  "endDate": "2024-01-02"
}
```
Response:
```

  {
    "name": "string",
    "id": "string",
    "diameterMin": 0,
    "diameterMax": 0,
    "isPotentiallyHazardousAsteroid": true,
    "absoluteMagnitudeH": 0,
    "closeApproachData": [
      {
        "closeApproachDate": "string",
        "relativeVelocity": 0,
        "missDistance": 0
      }
    ]
  }
]
```
### `/getDataFromDb`
This **GET** endpoint will fetch ten closest asteroids data from the database

Response:
```
[
  {
    "name": "string",
    "id": "string",
    "diameterMin": 0,
    "diameterMax": 0,
    "isPotentiallyHazardousAsteroid": true,
    "absoluteMagnitudeH": 0,
    "closeApproachData": [
      {
        "closeApproachDate": "string",
        "relativeVelocity": 0,
        "missDistance": 0
      }
    ]
  }
]
```
### You can access all of the request and response body documentation on [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) when you run the application
