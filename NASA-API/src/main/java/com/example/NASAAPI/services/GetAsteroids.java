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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetAsteroids {

    @Value("${nasa.api-key}")
    private String privateKey;
    private RestTemplate restTemplate = new RestTemplate();
    public List<Asteroid> getTenClosestAsteroids(String startDate, String endDate) throws Exception {
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
                    JSONObject asteroid = asteroidsByDate.getJSONObject(idx);Asteroid tempAsteroid = new Asteroid();
                    tempAsteroid.setId(asteroid.getString("id"));
                    tempAsteroid.setName(asteroid.getString("name"));
                    tempAsteroid.setAbsoluteMagnitudeH(asteroid.getBigDecimal("absolute_magnitude_h"));
                    tempAsteroid.setDiameterMin(asteroid.getJSONObject("estimated_diameter").getJSONObject("meters").getBigDecimal("estimated_diameter_min"));
                    tempAsteroid.setDiameterMax(asteroid.getJSONObject("estimated_diameter").getJSONObject("meters").getBigDecimal("estimated_diameter_max"));
                    tempAsteroid.setIsPotentiallyHazardousAsteroid(asteroid.getBoolean("is_potentially_hazardous_asteroid"));
                    List<CloseApproachData> closeApproachDataList = new ArrayList<>();
                    JSONArray resultCloseApproachData = asteroid.getJSONArray("close_approach_data");
                    for(int index=0; index<resultCloseApproachData.length();index++){
                        CloseApproachData tempCloseApproachData = new CloseApproachData();
                        tempCloseApproachData.setCloseApproachDateFull(resultCloseApproachData.getJSONObject(index).getString("close_approach_date_full"));
                        tempCloseApproachData.setMissDistance(resultCloseApproachData.getJSONObject(index).getJSONObject("miss_distance").getBigDecimal("kilometers"));
                        tempCloseApproachData.setRelativeVelocity(resultCloseApproachData.getJSONObject(index).getJSONObject("relative_velocity").getBigDecimal("kilometers_per_hour"));
                        closeApproachDataList.add(tempCloseApproachData);
                    }
                    tempAsteroid.setCloseApproachData(closeApproachDataList);
                    asteroids.add(tempAsteroid);
                }
            }

        }
        asteroids= asteroids.stream()
                .sorted(((o1, o2) -> Double.compare(
                        o1.getCloseApproachData().stream().mapToDouble(o->o.getMissDistance().doubleValue()).min().getAsDouble(),
                        o2.getCloseApproachData().stream().mapToDouble(o->o.getMissDistance().doubleValue()).min().getAsDouble()
                )))
                .limit(10)
                .collect(Collectors.toList());
        return asteroids;
    }
}
