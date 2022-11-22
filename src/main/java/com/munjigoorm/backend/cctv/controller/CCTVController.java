package com.munjigoorm.backend.cctv.controller;

import com.munjigoorm.backend.cctv.service.CCTVService;
import com.munjigoorm.backend.map.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cctv")
public class CCTVController {

    @Autowired
    private CCTVService cctvService;

    @GetMapping
    public String mapInfo(@RequestParam double xOne, @RequestParam double xTwo, @RequestParam double yOne, @RequestParam double yTwo) {
        return cctvService.getCctvInfo(xOne, xTwo, yOne, yTwo);
    }
}
