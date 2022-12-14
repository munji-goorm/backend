package com.munjigoorm.backend.map.controller;

import com.munjigoorm.backend.map.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/map")
public class MapController {

    @Autowired
    private MapService mapService;

    @GetMapping
    public String mapInfo(@RequestParam int mapLevel, @RequestParam double xOne, @RequestParam double xTwo, @RequestParam double yOne, @RequestParam double yTwo) {
        return mapService.getMapInfo(mapLevel, xOne, xTwo, yOne, yTwo);
    }
}
