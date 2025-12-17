package com.project.aau.sw3.p3.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Service
public class GeusService {
    
    private static final String GEUS_URL = "https://google.com";

    public String fetchGeusData(){
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(GEUS_URL, String.class);
            String json = response.getBody();
            return json;

        } catch (Exception e) {
            return "Error fetching GEUS data" + e.getMessage();
        }
    }
}
