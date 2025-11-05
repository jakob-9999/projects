package com.project.aau.sw3.p3.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.project.aau.sw3.p3.service.SewerFeatureService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SewerController {

    private final SewerFeatureService sewerFeatureService;

    public SewerController(SewerFeatureService sewerFeatureService) {
        this.sewerFeatureService = sewerFeatureService;
    }

    @PostMapping( "/api/sewageland/features")
    public void createSewagelandFeatureCollection() {
        sewerFeatureService.createSewagelandFeatures();
    }

    @GetMapping("/api/sewageland/features")
    public ObjectNode getSewagelandFeatureCollection() {
        return sewerFeatureService.buildSewerFeatureCollection();
    }
}