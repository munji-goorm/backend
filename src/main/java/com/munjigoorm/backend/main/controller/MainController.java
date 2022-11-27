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
    public String airInfo(@RequestParam String latitude, @RequestParam String longitude) throws UnsupportedEncodingException {
        return mainService.getAirInfo(latitude, longitude);
    }

    @GetMapping(value = "/search")
    public String airInfo(@RequestParam String keyword) throws UnsupportedEncodingException {
        String searchKeyword = URLDecoder.decode(keyword, "UTF-8");
        return mainService.getRegionList(searchKeyword);
    }
}
