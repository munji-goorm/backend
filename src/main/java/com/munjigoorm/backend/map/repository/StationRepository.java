package com.munjigoorm.backend.map.repository;

import com.munjigoorm.backend.map.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, String> {

    Station findByAddr(String addr);
}
