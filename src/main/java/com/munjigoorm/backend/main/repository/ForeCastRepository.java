package com.munjigoorm.backend.main.repository;

import com.munjigoorm.backend.main.entity.ForeCast;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForeCastRepository extends JpaRepository<ForeCast, Integer> {

    ForeCast findByCityAndDateTime(String city, String dateTime);
}
