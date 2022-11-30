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

    @Column(name = "khai_state")
    private String khaiState;

    @Column(name = "pm10_value")
    private Integer pm10Value;

    @Column(name = "pm10_stateK")
    private String pm10StateK;

    @Column(name = "pm10_stateW")
    private String pm10StateW;

    @Column(name = "pm25_value")
    private Integer pm25Value;

    @Column(name = "pm25_stateK")
    private String pm25StateK;

    @Column(name = "pm25_stateW")
    private String pm25StateW;

    @Column(name = "o3_value")
    private Float o3Value;

    @Column(name = "o3_state")
    private String o3State;

    @Column(name = "co_value")
    private Float coValue;

    @Column(name = "co_state")
    private String coState;

    @Column(name = "no2_value")
    private Float no2Value;

    @Column(name = "no2_state")
    private String no2State;

    @Column(name = "so2_value")
    private Float so2Value;

    @Column(name = "so2_state")
    private String so2State;


    @Builder
    public Air(String stationName, String dateTime, String sidoName, String mangName, Integer khaiValue, String khaiState, Integer pm10Value, String pm10StateK, String pm10StateW, Integer pm25Value, String pm25StateK, String pm25StateW, Float o3Value, String o3State, Float coValue, String coState, Float no2Value, String no2State, Float so2Value, String so2State) {
        this.stationName = stationName;
        this.dateTime = dateTime;
        this.sidoName = sidoName;
        this.mangName = mangName;
        this.khaiValue = khaiValue;
        this.khaiState = khaiState;
        this.pm10Value = pm10Value;
        this.pm10StateK = pm10StateK;
        this.pm10StateW = pm10StateW;
        this.pm25Value = pm25Value;
        this.pm25StateK = pm25StateK;
        this.pm25StateW = pm25StateW;
        this.o3Value = o3Value;
        this.o3State = o3State;
        this.coValue = coValue;
        this.coState = coState;
        this.no2Value = no2Value;
        this.no2State = no2State;
        this.so2Value = so2Value;
        this.so2State = so2State;
    }
}
