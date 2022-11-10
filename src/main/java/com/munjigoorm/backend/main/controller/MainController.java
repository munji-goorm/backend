package com.munjigoorm.backend.main.controller;

import com.google.gson.JsonObject;
import com.munjigoorm.backend.main.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/main")
public class MainController {

    @Autowired
    private MainService mainService;

    @GetMapping
    public String airInfo(@RequestParam String stationName, @RequestParam String city) {
        return mainService.getAirInfo(stationName, city);
    }
}
