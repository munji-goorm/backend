package com.munjigoorm.backend.main.service;

import com.google.gson.*;
import com.munjigoorm.backend.main.dto.AddressResponse;
import com.munjigoorm.backend.main.dto.StationResponse;
import com.munjigoorm.backend.main.entity.Address;
import com.munjigoorm.backend.main.entity.Air;
import com.munjigoorm.backend.main.entity.ForeCast;
import com.munjigoorm.backend.main.repository.AddressRepository;
import com.munjigoorm.backend.main.repository.AirRepository;
import com.munjigoorm.backend.main.repository.ForeCastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MainService {

    public static final HashMap<String, String> cityStation;
    static{
        cityStation = new HashMap<>();
        cityStation.put("인천", "남동");
        cityStation.put("대구", "남산1동");
        cityStation.put("울산", "대송동");
        cityStation.put("대전", "둔산동");
        cityStation.put("세종", "보람동");
        cityStation.put("광주", "서석동");
        cityStation.put("경북", "영양군");
        cityStation.put("경기", "영통동");
        cityStation.put("충남", "예산군");
        cityStation.put("충북", "용담동");
        cityStation.put("전남", "용당동");
        cityStation.put("제주", "이도동");
        cityStation.put("서울", "중구");
        cityStation.put("강원", "중앙로");
        cityStation.put("부산", "초량동");
        cityStation.put("전북", "팔봉동");
        cityStation.put("경남", "회원동");
    }

    @Autowired
    private AirRepository airRepository;

    @Autowired
    private ForeCastRepository foreCastRepository;

    @Autowired
    private AddressRepository addressRepository;

    public String getAirInfo(String stationName, String addr) {
        JsonObject responseJson = new JsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // 측정소 주소로 city 알아내기
        String city = addr.substring(0,2);

        // 해당하는 stationName이 DB에 저장되어 있는지 확인
        Optional<Air> air = airRepository.findById(stationName);

        // 존재하는 경우
        if(air.isPresent()) {
            JsonObject data = new JsonObject();
            Air airResponse = air.get();
            responseJson.addProperty("status", 200);
            responseJson.addProperty("success", true);

            // 특정 측정소의 대기 정보 조회
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

            // 예보 정보 조회
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
            Calendar date = Calendar.getInstance();
            JsonObject foreCast = new JsonObject();

            for(int i=0; i<6; i++) {
                ForeCast fc = foreCastRepository.findByCityAndDateTime(city, sdf.format(date.getTime()));
                if(fc != null)
                    foreCast.addProperty(fc.getDateTime(), fc.getStatus());
                else
                    foreCast.addProperty(sdf.format(date.getTime()), "해당 날짜의 데이터가 존재하지 않습니다.");
                date.add(Calendar.DATE, 1);
            }
            data.add("forecast", foreCast);


            // 전국 통합대기질 정보 조회
            JsonObject nationwide = new JsonObject();
            for(String cityName : cityStation.keySet()) {
                String cityStationName = cityStation.get(cityName);
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

    public String getRegionList(String keyword) {
        JsonObject responseJson = new JsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        responseJson.addProperty("status", 200);
        responseJson.addProperty("success", true);

        List<Address> searchResult = addressRepository.findByFullAddrContaining(keyword);
        List<AddressResponse> addressResponses = new ArrayList<>();

        int count = 0;
        for(Address addr: searchResult) {
            if(count == 30) break;
            AddressResponse addressResponse = AddressResponse.builder()
                    .fullAddr(addr.getFullAddr())
                    .shortAddr(addr.getShortAddr())
                    .xCoord(addr.getXCoord())
                    .yCoord(addr.getYCoord())
                    .build();
            addressResponses.add(addressResponse);
            count++;
        }
        responseJson.add("data", JsonParser.parseString(gson.toJson(addressResponses)));

        return responseJson.toString();
    }

    public static String calStateFloat(float value, String type) {
        if(value < 0) return "점검중";

        switch (type) {
            case "o3":
                if(value <= 0.03) return "좋음";
                else if(value <= 0.09) return "보통";
                else if(value <= 0.15) return "나쁨";
                else return "최악";
            case "co":
                if(value <= 2.0) return "좋음";
                else if(value <= 9.0) return "보통";
                else if(value <= 15.0) return "나쁨";
                else return "최악";
            case "no2":
                if(value <= 0.03) return "좋음";
                else if(value <= 0.06) return "보통";
                else if(value <= 0.2) return "나쁨";
                else return "최악";
            case "so2":
                if(value <= 0.02) return "좋음";
                else if(value <= 0.05) return "보통";
                else if(value <= 0.15) return "나쁨";
                else return "최악";
            default:
                break;
        }
        return null;
    }

    public static String calStateInteger(int value, String type) {
        if(value < 0) return "점검중";

        switch (type) {
            case "khai":
                if(value <= 50) return "좋음";
                else if(value <= 100) return "보통";
                else if(value <= 250) return "나쁨";
                else return "최악";
            case "pm10K":
                if(value <= 30) return "좋음";
                else if(value <= 80) return "보통";
                else if(value <= 150) return "나쁨";
                else return "최악";
            case "pm10W":
                if(value <= 30) return "좋음";
                else if(value <= 50) return "보통";
                else if(value <= 100) return "나쁨";
                else return "최악";
            case "pm25K":
                if(value <= 15) return "좋음";
                else if(value <= 35) return "보통";
                else if(value <= 75) return "나쁨";
                else return "최악";
            case "pm25W":
                if(value <= 15) return "좋음";
                else if(value <= 25) return "보통";
                else if(value <= 50) return "나쁨";
                else return "최악";
            default:
                break;
        }
        return null;
    }
}
