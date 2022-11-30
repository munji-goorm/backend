package com.munjigoorm.backend.main.dto;

import lombok.*;

@Getter
@Setter
public class StationResponse{
    private String stationName;
    private String shortAddr;
    private String dateTime;
    private Integer khaiValue;
    private String khaiState;
    private Integer pm10Value;
    private String pm10StateK;
    private String pm10StateW;
    private Integer pm25Value;
    private String pm25StateK;
    private String pm25StateW;
    private Float o3Value;
    private String o3State;
    private Float coValue;
    private String coState;
    private Float no2Value;
    private String no2State;
    private Float so2Value;
    private String so2State;

    @Builder
    public StationResponse(String stationName, String shortAddr, String dateTime, Integer khaiValue, String khaiState, Integer pm10Value, String pm10StateK, String pm10StateW, Integer pm25Value, String pm25StateK, String pm25StateW, Float o3Value, String o3State, Float coValue, String coState, Float no2Value, String no2State, Float so2Value, String so2State) {
        this.stationName = stationName;
        this.shortAddr = shortAddr;
        this.dateTime = dateTime;
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
