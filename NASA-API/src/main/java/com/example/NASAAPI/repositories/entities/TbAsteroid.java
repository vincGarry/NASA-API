package com.example.NASAAPI.repositories.entities;

import com.example.NASAAPI.services.models.CloseApproachData;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity(name="com.example.NASAAPI.repositories.entities.TbAsteroid")
@Table(name="tb_asteroid")
public class TbAsteroid  implements Serializable {
    @Id
    @Column(name="tba_id",nullable = false)
    private String tbaId;
    @Column(name="tba_name",nullable = false)
    private String name;
    @Column(name="tba_diameter_min",nullable = false)
    private BigDecimal diameterMin;
    @Column(name="tba_diameter_max",nullable = false)
    private BigDecimal diameterMax;
    @Column(name="tba_is_hazardous",nullable = false)
    private Boolean isPotentiallyHazardousAsteroid;
    @Column(name="tba_abs_magnitude",nullable = false)
    private BigDecimal absoluteMagnitudeH;
}
