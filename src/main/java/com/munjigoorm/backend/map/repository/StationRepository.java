package com.munjigoorm.backend.map.repository;

import com.munjigoorm.backend.map.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StationRepository extends JpaRepository<Station, String> {

    //List<Station> findByDmXBetweenAndDmYBetween(Double dmX, Double dmX2, Double dmY, Double dmY2);
}
