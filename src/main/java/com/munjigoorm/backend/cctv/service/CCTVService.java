package com.munjigoorm.backend.cctv.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.munjigoorm.backend.cctv.entity.CCTV;
import com.munjigoorm.backend.cctv.repository.CCTVRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CCTVService {

    @Autowired
    CCTVRepository cctvRepository;

    public String getCctvInfo(double xOne, double xTwo, double yOne, double yTwo) {
        JsonObject responseJson = new JsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        responseJson.addProperty("status", 200);
        responseJson.addProperty("success", true);

        // 요청 받은 범위 내에 있는 측정소 정보 가져오기
        List<CCTV> cctvs = cctvRepository.findByXCoordBetweenAndYCoordBetween(xOne, xTwo, yOne, yTwo);
        responseJson.add("data", JsonParser.parseString(gson.toJson(cctvs)));

        return responseJson.toString();
    }
}
