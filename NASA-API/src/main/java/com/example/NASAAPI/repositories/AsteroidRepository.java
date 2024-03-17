package com.example.NASAAPI.repositories;

import com.example.NASAAPI.repositories.entities.TbAsteroid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AsteroidRepository extends JpaRepository<TbAsteroid,String> {
}
