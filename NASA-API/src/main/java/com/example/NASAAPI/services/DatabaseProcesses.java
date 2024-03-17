package com.example.NASAAPI.services;

import com.example.NASAAPI.repositories.AsteroidRepository;
import com.example.NASAAPI.repositories.CloseApproachDataRepository;
import com.example.NASAAPI.repositories.entities.TbAsteroid;
import com.example.NASAAPI.repositories.entities.TbCloseApproachData;
import com.example.NASAAPI.services.models.Asteroid;
import com.example.NASAAPI.services.models.CloseApproachData;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DatabaseProcesses {
    private final AsteroidRepository asteroidRepository;
    private final CloseApproachDataRepository closeApproachDataRepository;
    @Transactional
    public void InsertAsteroidsData(List<Asteroid> asteroids){
        //Deleting all data in database before inserting it from a new top 10 nearest asteroids
        asteroidRepository.deleteAll();
        closeApproachDataRepository.deleteAll();
        insertDataToTbAsteroid(asteroids);
        insertDataToTbCloseApproachData(asteroids);
    }
    void insertDataToTbAsteroid(List<Asteroid> asteroids){
        //Parse Asteroid Data Object into Entity using stream and insert it into DB using JPA
        List<TbAsteroid> asteroidsData = new ArrayList<>();
        asteroids.stream().map(asteroid->{
            TbAsteroid tbAsteroid = new TbAsteroid();
            tbAsteroid.setTbaId(asteroid.getId());
            tbAsteroid.setName(asteroid.getName());
            tbAsteroid.setIsPotentiallyHazardousAsteroid(asteroid.getIsPotentiallyHazardousAsteroid());
            tbAsteroid.setDiameterMin(asteroid.getDiameterMin());
            tbAsteroid.setDiameterMax(asteroid.getDiameterMax());
            tbAsteroid.setAbsoluteMagnitudeH(asteroid.getAbsoluteMagnitudeH());
            return  tbAsteroid;
        }).forEach(asteroid->asteroidsData.add(asteroid));
        asteroidRepository.saveAllAndFlush(asteroidsData);
    }

    void insertDataToTbCloseApproachData(List<Asteroid> asteroids){
        //Parse Close Approach Data Object into Entity using stream and insert it into DB using JPA
        List<TbCloseApproachData> closeApproachDataList = new ArrayList<>();
        asteroids.stream().forEach(asteroid -> {
            asteroid.getCloseApproachData().stream().map(closeApproachData -> {
                TbCloseApproachData tbCloseApproachData = new TbCloseApproachData();
                tbCloseApproachData.setTbcadId(asteroid.getId().concat(closeApproachData.getCloseApproachDate()));
                tbCloseApproachData.setAsteroidId(asteroid.getId());
                tbCloseApproachData.setMissDistance(closeApproachData.getMissDistance());
                tbCloseApproachData.setRelativeVelocity(closeApproachData.getRelativeVelocity());
                tbCloseApproachData.setCloseApproachDate(closeApproachData.getCloseApproachDate());
                return tbCloseApproachData;
            }).forEach(tbCloseApproachData -> closeApproachDataList.add(tbCloseApproachData));
        }
        );
        closeApproachDataRepository.saveAllAndFlush(closeApproachDataList);
    }
    @Transactional
    public List<Asteroid> getAllAsteroidsFromDB(){
        // Get All Asteroid Data From Database and parsing it into Object Using stream
       return asteroidRepository.findAll().stream().map(tbAsteroid->{
           Asteroid asteroid = new Asteroid();
           asteroid.setName(tbAsteroid.getName());
           asteroid.setId(tbAsteroid.getTbaId());
           asteroid.setIsPotentiallyHazardousAsteroid(tbAsteroid.getIsPotentiallyHazardousAsteroid());
           asteroid.setDiameterMin(tbAsteroid.getDiameterMin());
           asteroid.setDiameterMax(tbAsteroid.getDiameterMax());
           asteroid.setAbsoluteMagnitudeH(tbAsteroid.getAbsoluteMagnitudeH());
           //Getting All Close Approach Data using asteroid Id and parsing it into List of CloseApproachData Object
           List<TbCloseApproachData> closeApproachDataList = closeApproachDataRepository.getAllByAsteroidId(tbAsteroid.getTbaId());
           asteroid.setCloseApproachData(closeApproachDataList.stream().map(tbCloseApproachData->{
               CloseApproachData closeApproachData = new CloseApproachData();
               closeApproachData.setMissDistance(tbCloseApproachData.getMissDistance());
               closeApproachData.setCloseApproachDate(tbCloseApproachData.getCloseApproachDate());
               closeApproachData.setRelativeVelocity(tbCloseApproachData.getRelativeVelocity());
               return closeApproachData;
           }).collect(Collectors.toList()));
           return asteroid;
       }).collect(Collectors.toList());
    }
}
