package com.munjigoorm.backend.map.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapResponse {
    private String stationName;
    private Float dmX;
    private Float dmY;
    private Integer khaiValue;
    private String khaiState;
    private Integer pm10Value;
    private String pm10State;
    private Integer pm25Value;
    private String pm25State;
    private Float o3Value;
    private String o3State;
    private Float coValue;
    private String coState;
    private Float no2Value;
    private String no2State;
    private Float so2Value;
    private String so2State;

    @Builder
    public MapResponse(String stationName, Float dmX, Float dmY, Integer khaiValue, String khaiState, Integer pm10Value, String pm10State, Integer pm25Value, String pm25State, Float o3Value, String o3State, Float coValue, String coState, Float no2Value, String no2State, Float so2Value, String so2State) {
        this.stationName = stationName;
        this.dmX = dmX;
        this.dmY = dmY;
        this.khaiValue = khaiValue;
        this.khaiState = khaiState;
        this.pm10Value = pm10Value;
        this.pm10State = pm10State;
        this.pm25Value = pm25Value;
        this.pm25State = pm25State;
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
