package com.project.AAU_SW3_P3.kloakdata.controller;

import com.project.AAU_SW3_P3.kloakdata.service.KloakService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KloakController {

    private final KloakService kloakService;

    public KloakController(KloakService kloakService) {
        this.kloakService = kloakService;
    }

    @GetMapping(value= "/api/kloakopland", produces= "application/json")
    public String hentKloakOpland() throws Exception {
        return kloakService.hentKloakOpland();
    }
}
