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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MainService {
    @Value("${kakao_api_key}")
    private final String KAKAO_API_KEY = null;

    @Value("${open_api_key}")
    private final String OPEN_API_KEY = null;

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

    @Cacheable("main")
    public String getAirInfo(String latitude, String longitude) {
        // 위도와 경도를 통해 주소와 측정소 이름을 알아낸다.
        List<String> apiResult = getAddrAndStationNameAndShorAddr(latitude, longitude);
        String addr = apiResult.get(0);
        String stationName = apiResult.get(1);
        String shortAddr = apiResult.get(2);

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
                    .shorAddr(shortAddr)
                    .dateTime(airResponse.getDateTime())
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

    public List<String> getAddrAndStationNameAndShorAddr(String latitude, String longitude) {
        List<String> addrAndStationName = new ArrayList<>();

        // 위도, 경도 TM 좌표로 변환
        String tmApiUrl = "https://dapi.kakao.com/v2/local/geo/transcoord.json?" +
                "x=" + longitude + "&y=" + latitude +
                "&input_coord=WGS84&output_coord=TM";
        String tmJsonString = null;
        double tmX = 0, tmY = 0;

        tmJsonString = execApi(tmApiUrl, KAKAO_API_KEY);

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject)jsonParser.parse(tmJsonString);
        JsonArray documents = (JsonArray) jsonObject.get("documents");
        JsonObject tm = (JsonObject) documents.get(0);
        tmX = Double.parseDouble(tm.get("x").getAsString());
        tmY = Double.parseDouble(tm.get("y").getAsString());

        // TM좌표로 근접 측정소 찾아내기
        String nearApiUrl = "http://apis.data.go.kr/B552584/MsrstnInfoInqireSvc/getNearbyMsrstnList?" +
                "serviceKey=" + OPEN_API_KEY + "&returnType=json" +
                "&tmX=" + tmX + "&tmY=" + tmY;
        String nearJsonString = null;

        nearJsonString = execApi(nearApiUrl, OPEN_API_KEY);

        JsonObject nearJsonObject = (JsonObject)jsonParser.parse(nearJsonString);
        JsonObject response = (JsonObject)nearJsonObject.get("response");
        JsonObject body = (JsonObject)response.get("body");
        JsonArray items = (JsonArray)body.get("items");
        JsonObject nearStationInfo = (JsonObject) items.get(0);

        String addr = nearStationInfo.get("addr").getAsString();
        String stationName = nearStationInfo.get("stationName").getAsString();

        // 위도 경도로 주소 알아내기
        String addrApiUrl = "https://dapi.kakao.com/v2/local/geo/coord2address.json?" +
                "x=" + longitude + "&y=" + latitude +
                "&input_coord=WGS84";
        String addrJsonString = null;

        addrJsonString = execApi(addrApiUrl, KAKAO_API_KEY);

        JsonObject addrJsonObject = (JsonObject)jsonParser.parse(addrJsonString);
        JsonArray addrDocuments = (JsonArray)addrJsonObject.get("documents");
        JsonObject twoAddress = (JsonObject) addrDocuments.get(0);
        JsonObject address = (JsonObject) twoAddress.get("address");

        String shortAddr = address.get("region_2depth_name").toString() + " " + address.get("region_3depth_name").toString();
        shortAddr = shortAddr.replaceAll("\"", "");

        addrAndStationName.add(addr);
        addrAndStationName.add(stationName);
        addrAndStationName.add(shortAddr);

        return addrAndStationName;
    }

    public String execApi(String apiUrl, String apiKey) {
        try{
            URL url = new URL(apiUrl);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Authorization", "KakaoAK " + apiKey);

            BufferedReader rd = null;
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuffer docJson = new StringBuffer();

            String line;

            while ((line=rd.readLine()) != null) {
                docJson.append(line);
            }

            rd.close();
            return docJson.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
