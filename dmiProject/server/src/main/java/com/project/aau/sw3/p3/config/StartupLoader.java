package com.project.aau.sw3.p3.config;

import com.project.aau.sw3.p3.service.DmiService;
import com.project.aau.sw3.p3.service.SewerFeatureService;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Component
public class StartupLoader {

    private final SewerFeatureService sewerFeatureService;
    private final DmiService dmiService;

    public StartupLoader(SewerFeatureService sewerFeatureService, DmiService dmiService) {
        this.sewerFeatureService = sewerFeatureService;
        this.dmiService = dmiService;
    }

    @PostConstruct
    public void init() {
        sewerFeatureService.loadSewageLandData();
        dmiService.loadGridOnStartup();
    }
}