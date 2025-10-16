package com.project.aau.sw3.p3.controller;

import com.project.aau.sw3.p3.service.geusService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class geusController {

    private final geusService geusService;

    public geusController(geusService geusService) {
        this.geusService = geusService;
    }

    @GetMapping("/api/geus")
    public String getGeusData(){
        return geusService.fetchGeusData();
    }

}
