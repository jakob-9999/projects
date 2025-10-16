package com.project.aau.sw3.p3.controller;

import com.project.aau.sw3.p3.service.GeusService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeusController {

    private final GeusService geusService;

    public GeusController(GeusService geusService) {
        this.geusService = geusService;
    }

    @GetMapping("/api/geus")
    public String getGeusData(){
        return geusService.fetchGeusData();
    }

}
