package com.example.NASAAPI.controllers;


import com.example.NASAAPI.controllers.models.RequestData;
import com.example.NASAAPI.controllers.models.RequestDataDistance;
import com.example.NASAAPI.services.GetAsteroids;
import com.example.NASAAPI.services.models.Asteroid;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController("NasaApiController")
public class NASAApiController {

    private final GetAsteroids getAsteroids;
    @PostMapping("/getTenClosestAsteroids")
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200", description = "Success"))
    public ResponseEntity<List<Asteroid>> getTenClosestAsteroids(@RequestBody RequestData requestData) {
        List<Asteroid> getAsteroidsTenClosestAsteroids = new ArrayList<>();
        try{
            getAsteroidsTenClosestAsteroids.addAll(getAsteroids.getTenClosestAsteroids(requestData.getStartDate(), requestData.getEndDate(),null));
        } catch(Exception e){
            e.printStackTrace();
        }
        MDC.clear();
        return ResponseEntity.ok().body(getAsteroidsTenClosestAsteroids);
    }

    @PostMapping("/getTenClosestAsteroidsByDistance")
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200", description = "Success"))
    public ResponseEntity<List<Asteroid>> getAsteroidByDistance(@RequestBody RequestDataDistance requestDataDistance) {
        List<Asteroid> getAsteroidsTenClosestAsteroids = new ArrayList<>();
        try{
            getAsteroidsTenClosestAsteroids.addAll(getAsteroids.getTenClosestAsteroids(requestDataDistance.getStartDate(), requestDataDistance.getEndDate(),requestDataDistance.getDistance()));
        } catch(Exception e){
            e.printStackTrace();
        }
        MDC.clear();
        return ResponseEntity.ok().body(getAsteroidsTenClosestAsteroids);
    }

    @PostMapping("/getSpecificAsteroidById")
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200", description = "Success"))
    public ResponseEntity<Asteroid> getSpecificAsteroidById(@RequestBody String asteroidId) {
        Asteroid result = new Asteroid();
        try{
            result = getAsteroids.getSpecificAsteroid(asteroidId);
        } catch(Exception e){
            e.printStackTrace();
        }
        MDC.clear();
        return ResponseEntity.ok().body(result);
    }
}
