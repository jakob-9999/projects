package com.project.aau.sw3.p3.controller;

import com.project.aau.sw3.p3.model.TotalPrecipitation;
import com.project.aau.sw3.p3.model.DmiPoint;
import com.project.aau.sw3.p3.repository.DmiPointRepo;
import com.project.aau.sw3.p3.service.DmiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController // Tells Spring, this class hadnles http request
@RequestMapping("/api")
public class DmiController {

    private final DmiPointRepo dmiPointRepo;
    private final DmiService dmiService;

    // Constructor injection – dette fortæller spring, at dmicontroller har brug for dmiservice.
    public DmiController(DmiPointRepo dmiPointRepo, DmiService dmiService) {
        this.dmiPointRepo = dmiPointRepo;
        this.dmiService = dmiService;
    }

    // GET-endpoint, som du kan kalde i browseren
    @GetMapping("/dmi")
    public Map<String, Object> getDmiData() {
        // Calls fetchDmiData from DmiService and that method returns json, why it shows in browser
            return dmiService.fetchDmiData();
    }

    @GetMapping("/dmi/precipitation")
    public TotalPrecipitation getTotalPrecipitation() {
        return dmiService.fetchTotalPrecipitation();
    }

    // TODO: Temporary, should be changed to take coordinates, id or another passed in parameter instead of hardcoding an id
    // Fetches the dmiPoint with ID=1
    @GetMapping("/dmi-dini/point")
    public DmiPoint getDmiDiniPoint() {
        return dmiPointRepo.findById(1L).orElse(null);
    }

    // persists a single Point from the DINI model from DMI Open Data in our DB
    @PostMapping("/dmi-dini/point")
    public void saveDmiDiniPoint() {
        dmiService.saveDmiDiniPoint();
    }

    //adds single point fro bbox to DB
    @PostMapping("/dmi-dini-bbox/bbox-point")
    public void saveBBox() {
        dmiService.saveBBox();
    }
}

