package com.project.aau.sw3.p3.controller;

import com.project.aau.sw3.p3.service.KloakService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KloakController {

    private final KloakService kloakService;

    public KloakController(KloakService kloakService) {
        this.kloakService = kloakService;
    }

    @GetMapping(value= "/api/kloakopland", produces= "application/json")
    public String henterKloakOpland() throws Exception {
        return kloakService.hentKloakOpland();
    }
}
