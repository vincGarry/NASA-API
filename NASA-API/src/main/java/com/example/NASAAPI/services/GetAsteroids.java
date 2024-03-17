package com.example.NASAAPI.services;

import com.example.NASAAPI.services.models.Asteroid;
import com.example.NASAAPI.services.models.CloseApproachData;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetAsteroids {

    @Value("${nasa.api-key}")
    private String privateKey;
    private RestTemplate restTemplate = new RestTemplate();
    public List<Asteroid> getTenClosestAsteroids(String startDate, String endDate,String distance) throws Exception {
        //Checking duration between 2 dates not exceeding 7 days following NASA Open API rules
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date firstDate = format.parse(startDate);
        Date secondDate = format.parse(endDate);
        long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        if(diff>7){
            throw new Exception("Date Exception - The Feed date limit is only 7 Days");
        }
        //get request to NASA Open API
        String NasaApiUrl = String.format("https://api.nasa.gov/neo/rest/v1/feed?start_date=%s&end_date=%s&api_key=%s",startDate,endDate,privateKey);
        System.out.println(NasaApiUrl);
        ResponseEntity<String> resultApi =
                restTemplate.getForEntity(NasaApiUrl , String.class);

        JSONObject resultObject = new JSONObject(resultApi.getBody());
        List<Asteroid> asteroids = new ArrayList<>();
        //iterate through near_earth_objects to be merged as 1 list of Asteroid Objects
        JSONObject nearEarthObjects = resultObject.getJSONObject("near_earth_objects");
        Iterator<String> keys = nearEarthObjects.keys();
        while(keys.hasNext()){
            String key = keys.next();
            if(nearEarthObjects.get(key) instanceof JSONArray){
                JSONArray asteroidsByDate = nearEarthObjects.getJSONArray(key);
                for(int idx=0; idx<asteroidsByDate.length();idx++){
                    JSONObject asteroid = asteroidsByDate.getJSONObject(idx);
                    asteroids.add(ParseAsteroidData(asteroid));
                }
            }

        }
        asteroids= sortAsteroidByDistance(asteroids,distance);
        return asteroids;
    }

    public List<Asteroid> sortAsteroidByDistance(List<Asteroid> asteroids, String distance){
        BigDecimal distanceLimit = new BigDecimal(0);
        if (Objects.nonNull(distance)){
            distanceLimit = new BigDecimal(distance);
        }
        final BigDecimal limitMinimum = distanceLimit;
        asteroids = asteroids.stream()
                .filter( asteroid -> new BigDecimal(
                        asteroid.getCloseApproachData().stream().mapToDouble(o->o.getMissDistance().doubleValue()).min().getAsDouble()
                        ).compareTo(limitMinimum)>=0
                )
                .sorted(((o1, o2) -> Double.compare(
                        o1.getCloseApproachData().stream().mapToDouble(o->o.getMissDistance().doubleValue()).min().getAsDouble(),
                        o2.getCloseApproachData().stream().mapToDouble(o->o.getMissDistance().doubleValue()).min().getAsDouble()
                )))
                .limit(10)
                .collect(Collectors.toList());
        return asteroids;
    }

    public Asteroid getSpecificAsteroid(String asteroidId){
        String NasaApiUrl = String.format("https://api.nasa.gov/neo/rest/v1/neo/%s?api_key=%s",asteroidId,privateKey);
        ResponseEntity<String> resultApi =
                restTemplate.getForEntity(NasaApiUrl , String.class);
        JSONObject result = new JSONObject(resultApi.getBody());
        return ParseAsteroidData(result);
    }
    public Asteroid ParseAsteroidData(JSONObject asteroid){
        //parsing API Response Data Into Objects
        Asteroid tempAsteroid = new Asteroid();
        tempAsteroid.setId(asteroid.getString("id"));
        tempAsteroid.setName(asteroid.getString("name"));
        tempAsteroid.setAbsoluteMagnitudeH(new BigDecimal(asteroid.getDouble("absolute_magnitude_h")));
        tempAsteroid.setDiameterMin(new BigDecimal(asteroid.getJSONObject("estimated_diameter").getJSONObject("meters").getDouble("estimated_diameter_min")));
        tempAsteroid.setDiameterMax(new BigDecimal(asteroid.getJSONObject("estimated_diameter").getJSONObject("meters").getDouble("estimated_diameter_max")));
        tempAsteroid.setIsPotentiallyHazardousAsteroid(asteroid.getBoolean("is_potentially_hazardous_asteroid"));
        List<CloseApproachData> closeApproachDataList = new ArrayList<>();
        JSONArray resultCloseApproachData = asteroid.getJSONArray("close_approach_data");
        for(int index=0; index<resultCloseApproachData.length();index++){
            CloseApproachData tempCloseApproachData = new CloseApproachData();
            tempCloseApproachData.setCloseApproachDate(resultCloseApproachData.getJSONObject(index).getString("close_approach_date"));
            tempCloseApproachData.setMissDistance(new BigDecimal(resultCloseApproachData.getJSONObject(index).getJSONObject("miss_distance").getDouble("kilometers")));
            tempCloseApproachData.setRelativeVelocity(new BigDecimal(resultCloseApproachData.getJSONObject(index).getJSONObject("relative_velocity").getDouble("kilometers_per_hour")));
            closeApproachDataList.add(tempCloseApproachData);
        }
        tempAsteroid.setCloseApproachData(closeApproachDataList);
        return tempAsteroid;
    }
}
