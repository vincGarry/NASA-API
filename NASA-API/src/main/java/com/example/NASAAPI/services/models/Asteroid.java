package com.example.NASAAPI.services.models;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Asteroid {
    private String name;
    private String id;
    private BigDecimal diameterMin;
    private BigDecimal diameterMax;
    private Boolean isPotentiallyHazardousAsteroid;
    private BigDecimal absoluteMagnitudeH;
    private List<CloseApproachData> closeApproachData;
}
