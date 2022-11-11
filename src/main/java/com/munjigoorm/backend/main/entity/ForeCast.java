package com.munjigoorm.backend.main.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "rt_air_forecast")
public class ForeCast {

    @Id
    @Column(name = "id")
    private Integer forecastId;

    @Column(name = "date_time")
    private String dateTime;

    @Column(name = "city")
    private String city;

    @Column(name = "status")
    private String status;
}
