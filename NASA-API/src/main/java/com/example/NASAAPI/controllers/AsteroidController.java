package com.example.NASAAPI.controllers;

import com.example.NASAAPI.controllers.models.RequestData;
import com.example.NASAAPI.services.GetAsteroids;
import com.example.NASAAPI.services.models.Asteroid;
import com.example.NASAAPI.services.models.CloseApproachData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AsteroidController {
    private final GetAsteroids getAsteroids;

    @GetMapping()
    String indexPage(Model model){
        RequestData requestData = new RequestData();
        model.addAttribute("requestData",requestData);
        return "index.html";
    }

    @PostMapping("/asteroids")
    String getAsteroid(Model model,
                       @ModelAttribute("requestData") RequestData requestData){
        model.addAttribute("title",String.format("Top 10 Nearest Asteroids List between %s to %s",requestData.getStartDate(), requestData.getEndDate()));
        List<Asteroid> asteroids = new ArrayList<>();
        List<String> closeApproachDataJsonString = new ArrayList<>();
        try {
            asteroids=getAsteroids.getTenClosestAsteroids(requestData.getStartDate(), requestData.getEndDate());
            closeApproachDataJsonString = asteroids.stream().map(Asteroid::getCloseApproachData).map( o->{
                ObjectMapper om = new ObjectMapper();
                try {
                    return om.writeValueAsString(o);
                }
                catch(Exception e){
                    return o.toString();
                }
            }
            ).collect(Collectors.toList());
            System.out.println(asteroids);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("closeApproachData",closeApproachDataJsonString);
        model.addAttribute("asteroids",asteroids);
        return "Asteroid.html";
    }

    @PostMapping("/closeapproachdata")
    String getCloseApproachData(Model model,
                                @ModelAttribute("closeApproachData") String closeApproachDataList,
                                @ModelAttribute("asteroidName") String name){
        System.out.println(closeApproachDataList);
        ObjectMapper om = new ObjectMapper();
        List<CloseApproachData> closeApproachData = new ArrayList<>();
        try {
            closeApproachData = Arrays.asList(om.readValue(closeApproachDataList,CloseApproachData[].class));
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(closeApproachData);
        model.addAttribute("title",String.format("Close Approach Data for Asteroid %s",name));
        model.addAttribute("closeApproachDataList",closeApproachData);
        return "CloseApproachData.html";
    }

}
