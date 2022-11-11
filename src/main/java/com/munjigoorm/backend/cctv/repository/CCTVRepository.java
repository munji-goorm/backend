package com.munjigoorm.backend.cctv.repository;

import com.munjigoorm.backend.cctv.entity.CCTV;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CCTVRepository extends JpaRepository<CCTV, String> {
}
