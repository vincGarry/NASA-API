package com.example.NASAAPI.repositories;

import com.example.NASAAPI.repositories.entities.TbCloseApproachData;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CloseApproachDataRepository extends JpaRepository<TbCloseApproachData,Long> {
    @Transactional(readOnly=true)
    List<TbCloseApproachData> getAllByAsteroidId(String asteroidId);
}
