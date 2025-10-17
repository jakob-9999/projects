package com.project.aau.sw3.p3.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.Map;

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

    public Map <String, Object> fetchDmiData() {
        try {
            // For HTTP requests
            RestTemplate restTemplate = new RestTemplate();

            // GET-request to DMI’s API, get answer as String)
            ResponseEntity<String> response = restTemplate.getForEntity(DMI_URL, String.class);

            // Save as JSON String
            String json = response.getBody();

            //converts JSON to a Map
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> data = mapper.readValue(json, Map.class);

            System.out.println("DMI API Response:");
            //System.out.println(json);

            //just an example: will look at the type
            System.out.println("JSON type: " + data.get("type"));

            // return the map in browser
            return data;

        } catch (Exception e) {
            //returns error as JSON
            return Map.of("error", "Fejl ved hentning af DMI data: " + e.getMessage());
        }
    }
}
