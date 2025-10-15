package com.project.aau.sw3.p3.controller;

import com.project.aau.sw3.p3.service.SewerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SewerController {

    private final SewerService sewerService;

    public SewerController(SewerService sewerService) {
        this.sewerService = sewerService;
    }

    @GetMapping(value= "/api/kloakopland", produces= "application/json")
    public String getSewageFromService() throws Exception {
        return sewerService.getSewageLand();
    }
}
