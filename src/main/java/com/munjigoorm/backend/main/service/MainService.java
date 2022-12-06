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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class MainService {
    public static final List<String> cityList;
    static {
        cityList = new ArrayList<>();
        cityList.add("울산");
        cityList.add("충북");
        cityList.add("서울");
        cityList.add("전북");
        cityList.add("경기");
        cityList.add("충남");
        cityList.add("부산");
        cityList.add("강원");
        cityList.add("경북");
        cityList.add("대전");
        cityList.add("세종");
        cityList.add("제주");
        cityList.add("대구");
        cityList.add("인천");
        cityList.add("전남");
        cityList.add("광주");
        cityList.add("경남");
    }

    @Autowired
    private AirRepository airRepository;

    @Autowired
    private ForeCastRepository foreCastRepository;

    @Autowired
    private AddressRepository addressRepository;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Scheduled(cron = "20 * * * * *")
    @CacheEvict(value = "main", allEntries = true)
    public void emptyMainCache() {
        logger.info("[CACHE_DELETE] empty main cache {}", LocalDateTime.now());
    }

    @Cacheable("main")
    public String getAirInfo(String fullAddr) {
        // fullAddr로 측정소 이름, city, shortAddr 알아내기
        String[] parsedAddr = fullAddr.split(" ");
        String stationName = null;
        String shortAddr = null;

        Optional<Address> locInfo = addressRepository.findById(fullAddr);
        if(locInfo.isPresent()) {
            Address address = locInfo.get();
            stationName = address.getStationName();
            shortAddr = address.getShortAddr();
        }

        JsonObject responseJson = new JsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // 측정소 주소로 city 알아내기
        String city = parsedAddr[0];

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
                    .stationName(stationName)
                    .shortAddr(shortAddr)
                    .dateTime(airResponse.getDateTime())
                    .khaiValue(airResponse.getKhaiValue())
                    .khaiState(airResponse.getKhaiState())
                    .pm10Value(airResponse.getPm10Value())
                    .pm10StateK(airResponse.getPm10StateK())
                    .pm10StateW(airResponse.getPm10StateW())
                    .pm25Value(airResponse.getPm25Value())
                    .pm25StateK(airResponse.getPm25StateK())
                    .pm25StateW(airResponse.getPm25StateK())
                    .o3Value(airResponse.getO3Value())
                    .o3State(airResponse.getO3State())
                    .coValue(airResponse.getCoValue())
                    .coState(airResponse.getCoState())
                    .no2Value(airResponse.getNo2Value())
                    .no2State(airResponse.getNo2State())
                    .so2Value(airResponse.getSo2Value())
                    .so2State(airResponse.getSo2State())
                    .build();
            data.add("stationInfo", JsonParser.parseString(gson.toJson(stationResponse)));

            // 예보 정보 조회
            SimpleDateFormat out = new SimpleDateFormat("MM.dd");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar date = Calendar.getInstance();
            JsonObject foreCast = new JsonObject();

            for(int i=0; i<6; i++) {
                ForeCast fc = foreCastRepository.findByCityAndDateTime(city, sdf.format(date.getTime()));
                if(fc != null)
                    foreCast.addProperty(out.format(date.getTime()), fc.getStatus());
                else
                    foreCast.addProperty(out.format(date.getTime()), "모름");
                date.add(Calendar.DATE, 1);
            }
            data.add("forecast", foreCast);


            // 전국 통합대기질 정보 조회
            JsonObject nationwide = new JsonObject();
            JsonObject nationwideValue = new JsonObject();
            for(String cityName: cityList) {
                List<Air> cityAirs = airRepository.findBySidoName(cityName);
                int normalCnt = 0;
                int totalKhaiValue = 0;
                for(Air cityAir: cityAirs) {
                    if(cityAir.getKhaiValue() != -1) {
                        normalCnt++;
                        totalKhaiValue += cityAir.getKhaiValue();
                    }
                }
                int avgKhaiValue = totalKhaiValue / normalCnt;
                nationwide.addProperty(cityName, calStateInteger(avgKhaiValue, "khai"));
                nationwideValue.addProperty(cityName, avgKhaiValue);
            }

            data.add("nationwide", nationwide);
            data.add("nationwideValue", nationwideValue);

            responseJson.add("data", data);
        } else{
            responseJson.addProperty("status", 200);
            responseJson.addProperty("success", false);
            responseJson.addProperty("message", "해당하는 측정소가 없습니다.");
        }

        return responseJson.toString();
    }

    @Cacheable("search")
    public String getRegionList(String keyword) {
        JsonObject responseJson = new JsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        responseJson.addProperty("status", 200);
        responseJson.addProperty("success", true);

        List<Address> searchResult = addressRepository.findTop30ByFullAddrContaining(keyword);
        List<AddressResponse> addressResponses = new ArrayList<>();

        for(Address addr: searchResult) {
            AddressResponse addressResponse = AddressResponse.builder()
                    .fullAddr(addr.getFullAddr())
                    .shortAddr(addr.getShortAddr())
                    .xCoord(addr.getXCoord())
                    .yCoord(addr.getYCoord())
                    .build();
            addressResponses.add(addressResponse);
        }
        responseJson.add("data", JsonParser.parseString(gson.toJson(addressResponses)));

        return responseJson.toString();
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
