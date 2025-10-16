package com.project.aau.sw3.p3.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Service
public class DmiService {

    // API Url der henter data ( 4 parametre)
    String DMI_URL =
            "https://dmigw.govcloud.dk/v1/forecastedr/collections/harmonie_dini_sf/position"
                    + "?coords=POINT(10.2039 56.1629)"
                    + "&parameter-name=total-precipitation"
                    + "&parameter-name=precipitation-type"
                    + "&parameter-name=time-integral-of-total-solid-precipitation-flux"
                    + "&parameter-name=rain-precipitation-rate"
                    + "&api-key=39d54b14-ff57-4612-85de-f66333bd4b03";

    public String fetchDmiData() {
        try {
            // For HTTP requests
            RestTemplate restTemplate = new RestTemplate();

            // GET-request to DMI’s API, get answer as String)
            ResponseEntity<String> response = restTemplate.getForEntity(DMI_URL, String.class);

            // Save as JSON String
            String json = response.getBody();

            System.out.println("DMI API Response:");
            System.out.println(json);

            // return in browser
            return json;

        } catch (Exception e) {
            return "Error fetching DMI data: " + e.getMessage();
        }
    }
}
