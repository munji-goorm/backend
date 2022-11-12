package com.munjigoorm.backend.main.controller;

import com.munjigoorm.backend.main.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/main")
public class MainController {

    @Autowired
    private MainService mainService;

    @GetMapping
    public String airInfo(@RequestParam String stationName, @RequestParam String addr) {
        return mainService.getAirInfo(stationName, addr);
    }

    @GetMapping(value = "/search")
    public String airInfo(@RequestParam String keyword) {
        return mainService.getRegionList(keyword);
    }
}
