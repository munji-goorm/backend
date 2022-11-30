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

@Service
public class MapService {

    @Autowired
    private AirRepository airRepository;

    @Autowired
    private StationRepository stationRepository;

    @Cacheable("map")
    public String getMapInfo(int mapLevel, double xOne, double xTwo, double yOne, double yTwo) {
        JsonObject responseJson = new JsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        responseJson.addProperty("status", 200);
        responseJson.addProperty("success", true);

        List<Station> stations = new ArrayList<>();
        List<MapResponse> mapResponses = new ArrayList<>();

        // mapLevel에 따라서 분할하는지 확인
        if(mapLevel >= 1 && mapLevel <= 8) {
            // 요청한 위도, 경도 안에 있는 측정소의 x좌표, y좌표 모두 가져오기
            stations = stationRepository.findByDmXBetweenAndDmYBetween(xOne, xTwo, yOne, yTwo);
            mapResponses = getStationInfo(stations, mapResponses, "all", 0);
        } else if(mapLevel == 9) {
            // 총 4구역으로 나누고, 한 구역별로 10개씩 가져오기
            double xDiff = (xTwo - xOne) / 2.0;
            double yDiff = (yTwo - yOne) / 2.0;

            stations = stationRepository.findByDmXBetweenAndDmYBetween(xOne, xOne+xDiff, yOne, yOne+yDiff);
            mapResponses = getStationInfo(stations, mapResponses, "limit", 10);
            stations = stationRepository.findByDmXBetweenAndDmYBetween(xOne+xDiff, xTwo, yOne, yOne+yDiff);
            mapResponses = getStationInfo(stations, mapResponses, "limit", 10);
            stations = stationRepository.findByDmXBetweenAndDmYBetween(xOne, xOne+xDiff, yOne+yDiff, yTwo);
            mapResponses = getStationInfo(stations, mapResponses, "limit", 10);
            stations = stationRepository.findByDmXBetweenAndDmYBetween(xOne+xDiff, xTwo, yOne+yDiff, yTwo);
            mapResponses = getStationInfo(stations, mapResponses, "limit", 10);
        } else if(mapLevel == 10) {
            // 총 6구역으로 나누고, 한 구역별로 7개씩 가져오기
            double xDiff = (xTwo - xOne) / 3.0;
            double yDiff = (yTwo - yOne) / 2.0;

            stations = stationRepository.findByDmXBetweenAndDmYBetween(xOne, xOne+xDiff, yOne, yOne+yDiff);
            mapResponses = getStationInfo(stations, mapResponses, "limit", 7);
            stations = stationRepository.findByDmXBetweenAndDmYBetween(xOne+xDiff, xOne+xDiff*2, yOne, yOne+yDiff);
            mapResponses = getStationInfo(stations, mapResponses, "limit", 7);
            stations = stationRepository.findByDmXBetweenAndDmYBetween(xOne+xDiff*2, xTwo, yOne, yOne+yDiff);
            mapResponses = getStationInfo(stations, mapResponses, "limit", 7);

            stations = stationRepository.findByDmXBetweenAndDmYBetween(xOne, xOne+xDiff, yOne+yDiff, yTwo);
            mapResponses = getStationInfo(stations, mapResponses, "limit", 7);
            stations = stationRepository.findByDmXBetweenAndDmYBetween(xOne+xDiff, xOne+xDiff*2, yOne+yDiff, yTwo);
            mapResponses = getStationInfo(stations, mapResponses, "limit", 7);
            stations = stationRepository.findByDmXBetweenAndDmYBetween(xOne+xDiff*2, xTwo, yOne+yDiff, yTwo);
            mapResponses = getStationInfo(stations, mapResponses, "limit", 7);
        } else if(mapLevel == 11) {
            // 총 8구역으로 나누고, 한 구역별로 5개씩 가져오기
            double xDiff = (xTwo - xOne) / 4.0;
            double yDiff = (yTwo - yOne) / 2.0;

            stations = stationRepository.findByDmXBetweenAndDmYBetween(xOne, xOne+xDiff, yOne, yOne+yDiff);
            mapResponses = getStationInfo(stations, mapResponses, "limit", 5);
            stations = stationRepository.findByDmXBetweenAndDmYBetween(xOne+xDiff, xOne+xDiff*2, yOne, yOne+yDiff);
            mapResponses = getStationInfo(stations, mapResponses, "limit", 5);
            stations = stationRepository.findByDmXBetweenAndDmYBetween(xOne+xDiff*2, xOne+xDiff*3, yOne, yOne+yDiff);
            mapResponses = getStationInfo(stations, mapResponses, "limit", 5);
            stations = stationRepository.findByDmXBetweenAndDmYBetween(xOne+xDiff*3, xTwo, yOne, yOne+yDiff);
            mapResponses = getStationInfo(stations, mapResponses, "limit", 5);

            stations = stationRepository.findByDmXBetweenAndDmYBetween(xOne, xOne+xDiff, yOne+yDiff, yTwo);
            mapResponses = getStationInfo(stations, mapResponses, "limit", 5);
            stations = stationRepository.findByDmXBetweenAndDmYBetween(xOne+xDiff, xOne+xDiff*2, yOne+yDiff, yTwo);
            mapResponses = getStationInfo(stations, mapResponses, "limit", 5);
            stations = stationRepository.findByDmXBetweenAndDmYBetween(xOne+xDiff*2, xOne+xDiff*3, yOne+yDiff, yTwo);
            mapResponses = getStationInfo(stations, mapResponses, "limit", 5);
            stations = stationRepository.findByDmXBetweenAndDmYBetween(xOne+xDiff*3, xTwo, yOne+yDiff, yTwo);
            mapResponses = getStationInfo(stations, mapResponses, "limit", 5);
        } else {
            // 시도별로 한개씩 표시
            Optional<Station> seoul = stationRepository.findById("중구");
            if(seoul.isPresent()) stations.add(seoul.get());
            Optional<Station> ulsan = stationRepository.findById("대송동");
            if(ulsan.isPresent()) stations.add(ulsan.get());
            Optional<Station> chungbuk = stationRepository.findById("가덕면");
            if(chungbuk.isPresent()) stations.add(chungbuk.get());
            Optional<Station> jeonbuk = stationRepository.findById("계화면");
            if(jeonbuk.isPresent()) stations.add(jeonbuk.get());
            Optional<Station> gyeonggi = stationRepository.findById("가남읍");
            if(gyeonggi.isPresent()) stations.add(gyeonggi.get());
            Optional<Station> chungnam = stationRepository.findById("공주");
            if(chungnam.isPresent()) stations.add(chungnam.get());
            Optional<Station> busan = stationRepository.findById("개금동");
            if(busan.isPresent()) stations.add(busan.get());
            Optional<Station> gangwon = stationRepository.findById("갈말읍");
            if(gangwon.isPresent()) stations.add(gangwon.get());
            Optional<Station> gyeongbuk = stationRepository.findById("평화남산동");
            if(gyeongbuk.isPresent()) stations.add(gyeongbuk.get());
            Optional<Station> daejeon = stationRepository.findById("둔산동");
            if(daejeon.isPresent()) stations.add(daejeon.get());
            Optional<Station> sejong = stationRepository.findById("부강면");
            if(sejong.isPresent()) stations.add(sejong.get());
            Optional<Station> jeju = stationRepository.findById("강정동");
            if(jeju.isPresent()) stations.add(jeju.get());
            Optional<Station> daegu = stationRepository.findById("산격동");
            if(daegu.isPresent()) stations.add(daegu.get());
            Optional<Station> incheon = stationRepository.findById("송도");
            if(incheon.isPresent()) stations.add(incheon.get());
            Optional<Station> jeonnam = stationRepository.findById("여천동(여수)");
            if(jeonnam.isPresent()) stations.add(jeonnam.get());
            Optional<Station> gwangju = stationRepository.findById("서석동");
            if(gwangju.isPresent()) stations.add(gwangju.get());
            Optional<Station> gyeongnam = stationRepository.findById("경화동");
            if(gyeongnam.isPresent()) stations.add(gyeongnam.get());

            mapResponses = getStationInfo(stations, mapResponses, "all", 0);
        }
        responseJson.add("data", JsonParser.parseString(gson.toJson(mapResponses)));
        return responseJson.toString();
    }

    public List<MapResponse> getStationInfo(List<Station> stations, List<MapResponse> mapResponses, String type, int limit) {
        int count = 0;

        for (Station station : stations) {
            if(type.equals("limit") && count == limit) break;

            // stationName의 대기 오염 정보 조회
            Optional<Air> air = airRepository.findById(station.getStationName());

            if (air.isPresent()) {
                Air airResponse = air.get();
                MapResponse mapResponse = MapResponse.builder()
                        .stationName(station.getStationName())
                        .dmX(station.getDmX())
                        .dmY(station.getDmY())
                        .khaiValue(airResponse.getKhaiValue())
                        .khaiState(airResponse.getKhaiState())
                        .pm10Value(airResponse.getPm10Value())
                        .pm10State(airResponse.getPm10StateK())
                        .pm25Value(airResponse.getPm25Value())
                        .pm25State(airResponse.getPm25StateK())
                        .o3Value(airResponse.getO3Value())
                        .o3State(airResponse.getO3State())
                        .coValue(airResponse.getCoValue())
                        .coState(airResponse.getCoState())
                        .no2Value(airResponse.getNo2Value())
                        .no2State(airResponse.getNo2State())
                        .so2Value(airResponse.getSo2Value())
                        .so2State(airResponse.getSo2State())
                        .build();
                mapResponses.add(mapResponse);
            }
            count++;
        }
        return mapResponses;
    }
}
