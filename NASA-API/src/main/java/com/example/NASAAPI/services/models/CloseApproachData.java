package com.example.NASAAPI.services.models;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CloseApproachData {
    private String closeApproachDate;
    private BigDecimal relativeVelocity;
    private BigDecimal missDistance;
}
