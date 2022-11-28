package com.munjigoorm.backend.cctv.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.munjigoorm.backend.cctv.entity.CCTV;
import com.munjigoorm.backend.cctv.repository.CCTVRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CCTVService {

    @Autowired
    CCTVRepository cctvRepository;

    @Cacheable("cctv")
    public String getCctvInfo(int mapLevel, double xOne, double xTwo, double yOne, double yTwo) {
        JsonObject responseJson = new JsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        responseJson.addProperty("status", 200);
        responseJson.addProperty("success", true);

        List<CCTV> cctvs = null;
        List<CCTV> tmpCCTVS = null;

        // mapLevel에 따라서 분할하는지 확인
        if (mapLevel >= 1 && mapLevel <= 6) {
            // 요청한 위도, 경도 안에 있는 측정소의 x좌표, y좌표 모두 가져오기
            cctvs = cctvRepository.findByXCoordBetweenAndYCoordBetween(xOne, xTwo, yOne, yTwo);
        } else if (mapLevel >= 7 && mapLevel <= 10) {
            // 총 9구역으로 나누고, 한 구역별로 30개씩 가져오기
            double xDiff = (xTwo - xOne) / 3.0;
            double yDiff = (yTwo - yOne) / 3.0;

            // 요청 받은 범위 내에 있는 측정소 정보 가져오기
            cctvs = limitCCTV(cctvRepository.findByXCoordBetweenAndYCoordBetween(xOne, xOne+xDiff, yOne, yOne+yDiff));
            tmpCCTVS = cctvRepository.findByXCoordBetweenAndYCoordBetween(xOne+xDiff, xOne+xDiff*2, yOne, yOne+yDiff);
            cctvs.addAll(limitCCTV(tmpCCTVS));
            tmpCCTVS = cctvRepository.findByXCoordBetweenAndYCoordBetween(xOne+xDiff*2, xOne+xDiff*3, yOne, yOne+yDiff);
            cctvs.addAll(limitCCTV(tmpCCTVS));

            tmpCCTVS = cctvRepository.findByXCoordBetweenAndYCoordBetween(xOne, xOne+xDiff, yOne+yDiff, yOne+yDiff*2);
            cctvs.addAll(limitCCTV(tmpCCTVS));
            tmpCCTVS = cctvRepository.findByXCoordBetweenAndYCoordBetween(xOne+xDiff, xOne+xDiff*2, yOne+yDiff, yOne+yDiff*2);
            cctvs.addAll(limitCCTV(tmpCCTVS));
            tmpCCTVS = cctvRepository.findByXCoordBetweenAndYCoordBetween(xOne+xDiff*2, xOne+xDiff*3, yOne+yDiff, yOne+yDiff*2);
            cctvs.addAll(limitCCTV(tmpCCTVS));

            tmpCCTVS = cctvRepository.findByXCoordBetweenAndYCoordBetween(xOne, xOne+xDiff, yOne+yDiff*2, yOne+yDiff*3);
            cctvs.addAll(limitCCTV(tmpCCTVS));
            tmpCCTVS = cctvRepository.findByXCoordBetweenAndYCoordBetween(xOne+xDiff, xOne+xDiff*2, yOne+yDiff*2, yOne+yDiff*3);
            cctvs.addAll(limitCCTV(tmpCCTVS));
            tmpCCTVS = cctvRepository.findByXCoordBetweenAndYCoordBetween(xOne+xDiff*2, xOne+xDiff*3, yOne+yDiff*2, yOne+yDiff*3);
            cctvs.addAll(limitCCTV(tmpCCTVS));
        }

        responseJson.add("data", JsonParser.parseString(gson.toJson(cctvs)));

        return responseJson.toString();

    }

    public List<CCTV> limitCCTV(List<CCTV> cctvs) {
        int count = 0;
        List<CCTV> tmpCCTV = new ArrayList<>();
        for(CCTV cctv: cctvs) {
            if(count == 30) break;
            tmpCCTV.add(cctv);
            count++;
        }
        return tmpCCTV;
    }
}
