package com.example.NASAAPI.controllers;


import com.example.NASAAPI.controllers.models.RequestData;
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
            getAsteroidsTenClosestAsteroids.addAll(getAsteroids.getTenClosestAsteroids(requestData.getStartDate(), requestData.getEndDate()));
        } catch(Exception e){
            e.printStackTrace();
        }
        MDC.clear();
        return ResponseEntity.ok().body(getAsteroidsTenClosestAsteroids);
    }
}
