package com.munjigoorm.backend.main.entity;

import lombok.Builder;
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
@Table(name = "rt_air_info")
public class Air {

    @Id
    @Column(name = "station_name")
    private String stationName;

    @Column(name = "data_time")
    private String dateTime;

    @Column(name = "sido_name")
    private String sidoName;

    @Column(name = "mang_name")
    private String mangName;

    @Column(name = "khai_value")
    private Integer khaiValue;

    @Column(name = "pm10_value")
    private Integer pm10Value;

    @Column(name = "pm25_value")
    private Integer pm25Value;

    @Column(name = "o3_value")
    private Float o3Value;

    @Column(name = "co_value")
    private Float coValue;

    @Column(name = "no2_value")
    private Float no2Value;

    @Column(name = "so2_value")
    private Float so2Value;

    @Builder
    public Air(String stationName, String dateTime, String sidoName, String mangName, Integer khaiValue, Integer pm10Value, Integer pm25Value, Float o3Value, Float coValue, Float no2Value, Float so2Value) {
        this.stationName = stationName;
        this.dateTime = dateTime;
        this.sidoName = sidoName;
        this.mangName = mangName;
        this.khaiValue = khaiValue;
        this.pm10Value = pm10Value;
        this.pm25Value = pm25Value;
        this.o3Value = o3Value;
        this.coValue = coValue;
        this.no2Value = no2Value;
        this.so2Value = so2Value;
    }
}
