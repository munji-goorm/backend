package com.munjigoorm.backend.main.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "rt_nationwide_info")
public class CityStation {

    @Id
    @Column(name = "station_name")
    private String stationName;

    @Column(name = "city_name")
    private String cityName;

    @Builder
    public CityStation(String stationName, String cityName) {
        this.stationName = stationName;
        this.cityName = cityName;
    }
}
