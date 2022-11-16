package com.munjigoorm.backend.main.repository;

import com.munjigoorm.backend.main.entity.Air;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AirRepository extends JpaRepository<Air, String> {
    List<Air> findBySidoName(String sidoName);
}
