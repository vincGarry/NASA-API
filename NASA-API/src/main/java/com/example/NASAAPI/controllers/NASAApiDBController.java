package com.example.NASAAPI.controllers;

import com.example.NASAAPI.controllers.models.RequestData;
import com.example.NASAAPI.services.DatabaseProcesses;
import com.example.NASAAPI.services.GetAsteroids;
import com.example.NASAAPI.services.models.Asteroid;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController("NasaApiDBController")
public class NASAApiDBController {

    private final GetAsteroids getAsteroids;
    private final DatabaseProcesses dataBaseProcess;

    @PostMapping("/getTenClosestAsteroidAndSaveToDB")
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200", description = "Success"))
    public ResponseEntity<List<Asteroid>> getTenClosestAsteroidsAndSaveDataToDb(@RequestBody RequestData requestData) {
        List<Asteroid> getAsteroidsTenClosestAsteroids = new ArrayList<>();
        try{
            getAsteroidsTenClosestAsteroids.addAll(getAsteroids.getTenClosestAsteroids(requestData.getStartDate(), requestData.getEndDate()));
            dataBaseProcess.InsertAsteroidsData(getAsteroidsTenClosestAsteroids);
        } catch(Exception e){
            e.printStackTrace();
        }
        MDC.clear();
        return ResponseEntity.ok().body(getAsteroidsTenClosestAsteroids);
    }

    @GetMapping("/getDataFromDb")
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200", description = "Success"))
    public ResponseEntity<List<Asteroid>> getAsteroidsDatafromDb() {
        List<Asteroid> getTenClosestAsteroids = new ArrayList<>();
        try{
            getTenClosestAsteroids.addAll(dataBaseProcess.getAllAsteroidsFromDB());
            getTenClosestAsteroids = getAsteroids.sortAsteroidByDistance(getTenClosestAsteroids);
        } catch(Exception e){
            e.printStackTrace();
        }
        MDC.clear();
        return ResponseEntity.ok().body(getTenClosestAsteroids);
    }
}
