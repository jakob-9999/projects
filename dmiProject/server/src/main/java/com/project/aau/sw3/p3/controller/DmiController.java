package com.project.aau.sw3.p3.controller;

import com.project.aau.sw3.p3.service.DmiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

@RestController // Tells Spring, this class handel's http request
@RequestMapping("/api")
public class DmiController {

    private final DmiService dmiService;

    // Constructor injection – This tells Spring that the DmiController needs DmiService
    public DmiController(DmiService dmiService) {
        this.dmiService = dmiService;
    }

    // Persists every grid cell from the bbox in our DB
    @PostMapping("/dmi-dini-bbox/bbox-point")
    public void saveBBox() {
        dmiService.saveBBox();
    }

    @GetMapping("/dmi-dini-bbox/get-grid")
    public ObjectNode getGridCellFeatureCollection() {
        return dmiService.buildDmiGrid();
    }

    @PostMapping("/create-tif-files")
    public void createTifFiles() throws IOException {
        dmiService.projectGrids();
    }
}

