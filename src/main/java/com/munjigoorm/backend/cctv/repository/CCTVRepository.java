package com.munjigoorm.backend.cctv.repository;

import com.munjigoorm.backend.cctv.entity.CCTV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CCTVRepository extends JpaRepository<CCTV, String> {
    //List<CCTV> findByXCoordBetweenAndYCoordBetween(Double xCoord, Double xCoord2, Double yCoord, Double yCoord2);
}
