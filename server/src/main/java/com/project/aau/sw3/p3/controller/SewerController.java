package com.project.aau.sw3.p3.controller;

import com.project.aau.sw3.p3.service.SewerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import com.project.aau.sw3.p3.model.SewerData;
import com.project.aau.sw3.p3.repository.SewerDataRepo;
import java.util.List;


@RestController
public class SewerController {

    private final SewerService sewerService;
    private final SewerDataRepo sewerDataRepo;

    public SewerController(SewerService sewerService, SewerDataRepo sewerDataRepo) {
        this.sewerService = sewerService;
        this.sewerDataRepo = sewerDataRepo;
    }

    @PostMapping(value= "/api/sewageland", produces= "application/json")
    public String getSewageFromService() throws Exception {
        return sewerService.saveAndGetSewageLand();
    }


    @GetMapping("/api/sewerdata")
    //returns list of sewerData objects
    public List<SewerData> getSavedSewerData() {

        //sewerDataRepo.findAll() is a method that makes SQL request like SELECT * FROM sewer_data
        //it maps each row to sewerData object
        //collects all objects in List<SewerData>
        return sewerDataRepo.findAll();

    }
}