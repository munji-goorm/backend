package com.munjigoorm.backend.main.controller;

import com.munjigoorm.backend.main.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@RestController
@RequestMapping("/main")
public class MainController {

    @Autowired
    private MainService mainService;

    @GetMapping
    public String airInfo(@RequestParam String stationName, @RequestParam String addr) throws UnsupportedEncodingException {
        String station = URLDecoder.decode(stationName, "UTF-8");
        String address = URLDecoder.decode(addr, "UTF-8");
        return mainService.getAirInfo(station, address);
    }

    @GetMapping(value = "/search")
    public String airInfo(@RequestParam String keyword) throws UnsupportedEncodingException {
        String searchKeyword = URLDecoder.decode(keyword, "UTF-8");
        return mainService.getRegionList(searchKeyword);
    }
}
