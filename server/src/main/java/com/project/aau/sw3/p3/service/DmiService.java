package com.project.aau.sw3.p3.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.aau.sw3.p3.model.TotalPrecipitation;
import com.project.aau.sw3.p3.model.DmiPoint;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;
import com.project.aau.sw3.p3.repository.DmiPointRepo;
import org.springframework.beans.factory.annotation.Autowired;



@Service
public class DmiService {

    private final DmiPointRepo dmiPointRepo;

    //@Autowired lets us create an instance of a class
    @Autowired
    //constructor
    public DmiService(DmiPointRepo dmiPointRepo) {
        this.dmiPointRepo = dmiPointRepo;
    }

    // API Url der henter data ( 4 parametre)
    private static final String DMI_URL =
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

    public TotalPrecipitation fetchTotalPrecipitation() {
        try {
            // For HTTP requests
            RestTemplate restTemplate = new RestTemplate();

            // GET-request to DMI’s API, get answer as String)
            ResponseEntity<String> response = restTemplate.getForEntity(DMI_URL, String.class);

            // Save as JSON String
            String json = response.getBody();

            System.out.println("DMI API Response:");
            //System.out.println(json);


            ObjectMapper mapper = new ObjectMapper();

            //converts the whole JSON to a Map
            Map<String, Object> root = mapper.readValue(json, Map.class);

            //get the "ranges" part from the json
            Map<String, Object> ranges = (Map<String, Object>) root.get("ranges");

            //get the "total-precipitation" part, from the ranges part
            Object totalPrecipObj = ranges.get("total-precipitation");

            //convert to the model class "TotalPrecipitation"
            TotalPrecipitation tp = mapper.convertValue(totalPrecipObj, TotalPrecipitation.class);

            System.out.println("Number of values: " + tp.getValues().size());
            System.out.println("First value: " + tp.getValues().get(0));


            //just an example: will look at the type
            System.out.println("JSON type: " + root.get("type"));

            // return the map in browser
            return tp;

        } catch (Exception e) {
            //error message in console. tells where in the code it went wrong
            e.printStackTrace();
            return null;
        }
    }

    public DmiPoint fetchDmiPoint() {
        try {
            // For HTTP requests
            RestTemplate restTemplate = new RestTemplate();

            // GET-request to DMI’s API, get answer as String)
            ResponseEntity<String> response = restTemplate.getForEntity(DMI_URL, String.class);

            // Save as JSON String
            String json = response.getBody();

            //Create ObjectMapper to convert JSON strings into Java objects
            ObjectMapper mapper = new ObjectMapper();

            //converts the whole JSON to a Map
            Map<String, Object> root = mapper.readValue(json, Map.class);

            //get the "ranges" part from the json
            Map<String, Object> ranges = (Map<String, Object>) root.get("ranges");

            //get the "total-precipitation" part from the "ranges"-part
            Map<String, Object> totalPrecipitation = (Map<String, Object>) ranges.get("total-precipitation");

            //get the "values" from "total-precipitation" part, and save it as a list
            //totalPrecipitation.get("values") returns List<Object>, not List<Double>. Must be converted
            //mapper.convertValue takes two arguments: 1) what to convert, 2) what I want it converted to
            //TypeReference is an anonymous type that tells Jackson what type i want
            List<Double> precipitationValues = mapper.convertValue(totalPrecipitation.get("values"),
                    new TypeReference<List<Double>>() {}
            );

            //get the "domain" part from the json
            Map<String, Object> domain = (Map<String, Object>) root.get("domain");

            //get the "axes" part from the "domain"-part
            Map<String, Object> axes = (Map<String, Object>) domain.get("axes");

            //get the "x" part from the "axes"-part
            Map<String, Object> x = (Map<String, Object>) axes.get("x");

            //get the "values" from "x" part, convert to List<Double> and save it
            List<Double> xValues = mapper.convertValue(x.get("values"),
                    new TypeReference<List<Double>>() {}
            );

            //get the "bounds" from "x" part, convert to List<Double> and save it
            List<Double> xBounds = mapper.convertValue(x.get("bounds"),
                    new TypeReference<List<Double>>() {}
            );

            //get the "y" part from the "axes"-part
            Map<String, Object> y = (Map<String, Object>) axes.get("y");

            //get the "values" from "y" part, convert to List<Double> and save it
            List<Double> yValues = mapper.convertValue(y.get("values"),
                    new TypeReference<List<Double>>() {}
            );

            //get the "bounds" from "x" part, convert to List<Double> and save it
            List<Double> yBounds = mapper.convertValue(y.get("bounds"),
                    new TypeReference<List<Double>>() {}
            );

            //get the "t" part from the "axes"-part
            Map<String, Object> t = (Map<String, Object>) axes.get("t");

            //get the "valurs" from "t" part, convert to List<String> and save it. List<String> because time values are strings in json
            List<String> tValues = mapper.convertValue(t.get("values"),
                    new TypeReference<List<String>>() {}
            );

            //create DmiPoint object
            DmiPoint dp = new DmiPoint(precipitationValues, xValues, xBounds, yValues, yBounds, tValues);

            //save in db
            dmiPointRepo.save(dp);

            //return the map in browser
            return dp;

        } catch (Exception e) {
            //error message in console. tells where in the code it went wrong
            e.printStackTrace();
            return null;
        }
    }
}
