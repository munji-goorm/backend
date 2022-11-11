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

    public String getCctvInfo() {
        JsonObject responseJson = new JsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        responseJson.addProperty("status", 200);
        responseJson.addProperty("success", true);

        // 측정소 x좌표, y좌표 모두 가져오기
        List<CCTV> cctvs = cctvRepository.findAll();
        responseJson.add("data", JsonParser.parseString(gson.toJson(cctvs)));

        return responseJson.toString();
    }
}
