package com.example.NASAAPI.repositories.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity(name="com.example.NASAAPI.repositories.entities.TbCloseApproachData")
@Table(name="tb_close_approach_data")
@IdClass(TbCloseApproachData.PrimaryKeys.class)
public class TbCloseApproachData implements Serializable {
    @Data
    public static class PrimaryKeys implements Serializable{
        private String tbcadId;
    }
    @Id
    @Column(name="tbcad_id",nullable = false)
    private String tbcadId;
    @Column(name="tbcad_asteroid_id",nullable = false)
    private String asteroidId;
    @Column(name="tbcad_close_approach_date",nullable = false)
    private String closeApproachDate;
    @Column(name="tbcad_relative_velocity",nullable = false)
    private BigDecimal relativeVelocity;
    @Column(name="tbcad_miss_distance",nullable = false)
    private BigDecimal missDistance;
}
