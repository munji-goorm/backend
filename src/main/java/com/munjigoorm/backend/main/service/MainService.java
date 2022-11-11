package com.munjigoorm.backend.main.service;

import com.google.gson.*;
import com.munjigoorm.backend.main.dto.StationResponse;
import com.munjigoorm.backend.main.entity.Air;
import com.munjigoorm.backend.main.entity.CityStation;
import com.munjigoorm.backend.main.repository.AirRepository;
import com.munjigoorm.backend.main.repository.CityStationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MainService {

    @Autowired
    private AirRepository airRepository;

    @Autowired
    private CityStationRepository cityStationRepository;

    public String getAirInfo(String stationName, String city) {
        JsonObject responseJson = new JsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // 해당하는 stationName이 DB에 저장되어 있는지 확인
        Optional<Air> air = airRepository.findById(stationName);

        // 존재하는 경우
        if(air.isPresent()) {
            JsonObject data = new JsonObject();
            Air airResponse = air.get();
            responseJson.addProperty("status", 200);
            responseJson.addProperty("success", true);
            StationResponse stationResponse = StationResponse.builder()
                    .khaiValue(airResponse.getKhaiValue())
                    .khaiState(calStateInteger(airResponse.getKhaiValue(), "khai"))
                    .pm10Value(airResponse.getPm10Value())
                    .pm10StateK(calStateInteger(airResponse.getPm10Value(), "pm10K"))
                    .pm10StateW(calStateInteger(airResponse.getPm10Value(), "pm10W"))
                    .pm25Value(airResponse.getPm25Value())
                    .pm25StateK(calStateInteger(airResponse.getPm25Value(), "pm25K"))
                    .pm25StateW(calStateInteger(airResponse.getPm25Value(), "pm25W"))
                    .o3Value(airResponse.getO3Value())
                    .o3State(calStateFloat(airResponse.getO3Value(), "o3"))
                    .coValue(airResponse.getCoValue())
                    .coState(calStateFloat(airResponse.getCoValue(), "co"))
                    .no2Value(airResponse.getNo2Value())
                    .no2State(calStateFloat(airResponse.getNo2Value(), "no2"))
                    .so2Value(airResponse.getSo2Value())
                    .so2State(calStateFloat(airResponse.getSo2Value(), "so2"))
                    .build();
            data.add("stationInfo", JsonParser.parseString(gson.toJson(stationResponse)));


            /* 예보 정보 가져오기 */


            // 전국 통합대기질 정보 조회
            List<CityStation> cityStations = cityStationRepository.findAll();
            JsonObject nationwide = new JsonObject();
            for(CityStation cityStation: cityStations) {
                String cityName = cityStation.getCityName();
                String cityStationName = cityStation.getStationName();
                Optional<Air> cityAir = airRepository.findById(cityStationName);
                if(cityAir.isPresent()) {
                    Air cityAirResponse = cityAir.get();
                    nationwide.addProperty(cityName, calStateInteger(cityAirResponse.getKhaiValue(), "khai"));
                }
            }
            data.add("nationwide", nationwide);
            responseJson.add("data", data);
        } else{
            responseJson.addProperty("status", 200);
            responseJson.addProperty("success", false);
            responseJson.addProperty("message", "해당하는 측정소가 없습니다.");
        }

        return responseJson.toString();
    }

    public static String calStateFloat(float value, String type) {
        String status = "";
        if(value < 0) status = "통신오류";

        switch (type) {
            case "o3":
                if(value <= 0.03) status = "좋음";
                else if(value <= 0.09) status = "보통";
                else if(value <= 0.15) status = "나쁨";
                else status = "최악";
            case "co":
                if(value <= 2.0) status = "좋음";
                else if(value <= 9.0) status = "보통";
                else if(value <= 15.0) status = "나쁨";
                else status = "최악";
            case "no2":
                if(value <= 0.03) status = "좋음";
                else if(value <= 0.06) status = "보통";
                else if(value <= 0.2) status = "나쁨";
                else status = "최악";
            case "so2":
                if(value <= 0.02) status = "좋음";
                else if(value <= 0.05) status = "보통";
                else if(value <= 0.15) status = "나쁨";
                else status = "최악";
            default:
                break;
        }
        return status;
    }

    public static String calStateInteger(int value, String type) {
        String status = "";
        if(value < 0) status = "통신오류";

        switch (type) {
            case "khai":
                if(value <= 50) status = "좋음";
                else if(value <= 100) status = "보통";
                else if(value <= 250) status = "나쁨";
                else status = "최악";
            case "pm10K":
                if(value <= 30) status = "좋음";
                else if(value <= 80) status = "보통";
                else if(value <= 150) status = "나쁨";
                else status = "최악";
            case "pm10W":
                if(value <= 30) status = "좋음";
                else if(value <= 50) status = "보통";
                else if(value <= 100) status = "나쁨";
                else status = "최악";
            case "pm25K":
                if(value <= 15) status = "좋음";
                else if(value <= 35) status = "보통";
                else if(value <= 75) status = "나쁨";
                else status = "최악";
            case "pm25W":
                if(value <= 15) status = "좋음";
                else if(value <= 25) status = "보통";
                else if(value <= 50) status = "나쁨";
                else status = "최악";
            default:
                break;
        }
        return status;
    }
}
