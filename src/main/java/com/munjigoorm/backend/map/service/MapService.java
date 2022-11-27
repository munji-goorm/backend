package com.munjigoorm.backend.map.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.munjigoorm.backend.main.entity.Air;
import com.munjigoorm.backend.main.repository.AirRepository;
import com.munjigoorm.backend.map.dto.MapResponse;
import com.munjigoorm.backend.map.entity.Station;
import com.munjigoorm.backend.map.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.munjigoorm.backend.main.service.MainService.calStateFloat;
import static com.munjigoorm.backend.main.service.MainService.calStateInteger;

@Service
public class MapService {

    @Autowired
    private AirRepository airRepository;

    @Autowired
    private StationRepository stationRepository;

    @Cacheable("map")
    public String getMapInfo() {
        JsonObject responseJson = new JsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        responseJson.addProperty("status", 200);
        responseJson.addProperty("success", true);

        // 요청한 위도, 경도 안에 있는 측정소의 x좌표, y좌표 모두 가져오기
//        List<Station> stations = stationRepository.findByDmXBetweenAndDmYBetween(xOne, xTwo, yOne, yTwo);
        List<Station> stations = stationRepository.findAll();
        List<MapResponse> mapResponses = new ArrayList<>();

        for(Station station : stations) {
            // stationName의 대기 오염 정보 조회
            Optional<Air> air = airRepository.findById(station.getStationName());

            if(air.isPresent()) {
                Air airResponse = air.get();
                MapResponse mapResponse = MapResponse.builder()
                        .stationName(station.getStationName())
                        .dmX(station.getDmX())
                        .dmY(station.getDmY())
                        .khaiValue(airResponse.getKhaiValue())
                        .khaiState(calStateInteger(airResponse.getKhaiValue(), "khai"))
                        .pm10Value(airResponse.getPm10Value())
                        .pm10State(calStateInteger(airResponse.getPm10Value(), "pm10W"))
                        .pm25Value(airResponse.getPm25Value())
                        .pm25State(calStateInteger(airResponse.getPm25Value(), "pm25W"))
                        .o3Value(airResponse.getO3Value())
                        .o3State(calStateFloat(airResponse.getO3Value(), "o3"))
                        .coValue(airResponse.getCoValue())
                        .coState(calStateFloat(airResponse.getCoValue(), "co"))
                        .no2Value(airResponse.getNo2Value())
                        .no2State(calStateFloat(airResponse.getNo2Value(), "no2"))
                        .so2Value(airResponse.getSo2Value())
                        .so2State(calStateFloat(airResponse.getSo2Value(), "so2"))
                        .build();
                mapResponses.add(mapResponse);
            }
        }
        responseJson.add("data", JsonParser.parseString(gson.toJson(mapResponses)));
        return responseJson.toString();
    }
}
