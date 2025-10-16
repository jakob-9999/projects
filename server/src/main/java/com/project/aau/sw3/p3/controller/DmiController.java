package com.project.aau.sw3.p3.controller;

import com.project.aau.sw3.p3.service.DmiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Tells Spring, this class hadnles http request
public class DmiController {

    private final DmiService dmiService;

    // Constructor injection – dette fortæller spring, at dmicontroller har brug for dmiservice.
    public DmiController(DmiService dmiService) {
        this.dmiService = dmiService;
    }

    // GET-endpoint, som du kan kalde i browseren
    @GetMapping("/api/dmi")
    public String getDmiData() {
        // Calls fetchDmiData from DmiService and that method returns json, why it shows in browser
        return dmiService.fetchDmiData();
    }
}

